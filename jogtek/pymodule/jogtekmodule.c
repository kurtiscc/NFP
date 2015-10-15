#define _PYTHON
//#define _DEBUG

#ifdef _PYTHON
#include <Python.h>
#endif

/* This example program demonstrates how to read and write a raw data in a serial port.
Reading the serial port is handled with timeout. */

#include <stdio.h> // standard input / output functions
#include <string.h> // string function definitions
#include <unistd.h> // UNIX standard function definitions
#include <fcntl.h> // File control definitions
#include <errno.h> // Error number definitions
#include <termios.h> // POSIX terminal control definitionss
#include <time.h>   // time calls

int fd;
int read_port_flag = 0;
struct serial_data
{
    unsigned char  len; /* data length  */
    char data[32] __attribute__((aligned(32)));
};

int openPort(const char* port)
{
    fd = open(port, O_RDWR | O_NOCTTY);
    if(fd == -1)
    {
        return (-1);
    }
    else if (!strcmp(port, "/dev/ttyAMA0"))
    {
        // GPIO UART ttyAMA0
        struct termios options;
        tcgetattr(fd, &options);
        cfsetispeed(&options, B115200);
        cfsetospeed(&options, B115200);

        options.c_cflag &= ~PARENB;
        options.c_cflag &= ~CSTOPB;
        options.c_cflag &= ~CSIZE;
        options.c_cflag |= CS8;

        // no flow control
        options.c_cflag &= ~CRTSCTS;

        options.c_cflag |= CREAD | CLOCAL;  // turn on READ & ignore ctrl lines
        options.c_iflag &= ~(IXON | IXOFF | IXANY); // turn off s/w flow ctrl

        options.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG); // make raw
        options.c_oflag &= ~OPOST; // make raw

        // see: http://unixwiz.net/techtips/termios-vmin-vtime.html
        options.c_cc[VMIN]  = 0;
        options.c_cc[VTIME] = 20;

        if (tcsetattr(fd, TCSANOW, &options) < 0)
        {
            printf("init_serialport: Couldn't set term attributes\n");
            return -1;
        }
    }
    else if (!strcmp(port, "/dev/ttyUSB0"))
    {
        // FTDI ttyUSB0
        struct termios port_settings;
        bzero(&port_settings, sizeof(port_settings));

        /* set baud rates */
        cfsetispeed(&port_settings, B115200);
        cfsetospeed(&port_settings, B115200);

        /* enable the receiver and set local mode */
        port_settings.c_cflag |= (CLOCAL | CREAD);

        /* set no parity, stop bits, data bits */
        port_settings.c_cflag &= ~PARENB;
        port_settings.c_cflag &= ~CSTOPB;
        /* set character size as 8 bits */
        port_settings.c_cflag &= ~CSIZE;
        port_settings.c_cflag |= CS8;
        /* Raw input mode, sends the raw and unprocessed data  ( send as it is) */
        port_settings.c_lflag &= ~(ICANON | ECHO | ISIG);
        /* Raw output mode, sends the raw and unprocessed data  ( send as it is).
         * If it is in canonical mode and sending new line char then CR
         * will be added as prefix and send as CR LF
         */
        port_settings.c_oflag = ~OPOST;

        tcsetattr(fd, TCSANOW, &port_settings);
    }
    return 0;
}

int send_port0(struct serial_data *send_data)
{
    if ((write(fd, send_data->data, send_data->len)) != send_data->len)
    {
         return (-1);
    }

    return 0;
}

int send_port(const char *send_data)
{
    if ((write(fd, send_data, strlen(send_data))) != strlen(send_data))
    {
        printf("send_port fails\n");
        return (-1);
    }

    return 0;
}

void set_port_read_min_size(int min_size)
{
	struct termios settings;

	if (tcgetattr(fd, &settings))
	{
		/* handle error */
		return;
	}

	/* set the minimimum no. of chracters to read in each
	 * read call.
	 */
	settings.c_cc[VMIN]  = min_size;
	/* set \u201cread timeout between characters\u201d as 100 ms.
	 * read returns either when the specified number of chars
	 * are received or the timout occurs */
	settings.c_cc[VTIME] = 1; /* 1 * .1s */

	if (tcsetattr (fd, TCSANOW, &settings))
	{
		/* handle error */
	}
}

int read_port(char* buf, int size)
{
	int received = 0;
    struct serial_data recv_data;
    int recvbytes = 0;
    int maxfd = fd + 1;
    int index = 0;
    /* set the timeout as 1 sec for each read */
    struct timeval timeout = {0.01, 0};
    fd_set readSet;

    /* set the "mininum characters to read" */
    set_port_read_min_size(16);
    read_port_flag = 1;

    while (read_port_flag)
    {
        FD_ZERO(&readSet);
        FD_SET(fd, &readSet);
        read_port_flag = select(maxfd, &readSet, NULL, NULL, &timeout);
        if (read_port_flag > 0)
        {
            if (FD_ISSET(fd, &readSet))
            {
                recvbytes = read(fd, &recv_data.data, sizeof(recv_data.data));
                if (recvbytes)
                {
                    recv_data.len = recvbytes;
#ifdef _DEBUG
                    printf("data len = %d, data = <<", recvbytes);
#endif
                    for (index = 0; index < recvbytes; index++)
                    {
#ifdef _DEBUG
                        printf("%X ",recv_data.data[index]);
                        printf("%c",recv_data.data[index]);
#endif
						if (buf && (received < size)) 
						{
							buf[received++] = recv_data.data[index];
						}
                    }
#ifdef _DEBUG
                    printf(">>\n");
#endif
                }
                else
                {
                    /* handle error */
                }

            }
        }

        /* select() - returns 0 on timeout and -1 on error condtion */
        if (!read_port_flag)
        {
            return received;
        }
    }

    printf(" exit from read\n");
	return received;
}

void close_port()
{
    close(fd);
}

int send_command(const char* cmd, char* buf, int size)
{
	send_port(cmd);
	usleep(100000);
	return read_port(buf, size);
}

int open_reader(const char* port)
{
    //int ret = openPort("/dev/ttyUSB0");
    //int ret = openPort("/dev/ttyAMA0");
    int ret = openPort(port);
    if (ret == 0)
    {
	send_command("010A0003041000200000", 0, 0);

////send_port("0108000304FC0000");

	send_command("010C00030410002101000000", 0, 0);
	send_command("0109000304F0000000", 0, 0);
	send_command("0109000304F1FF0000", 0, 0);
    }
    else
    {
        printf("openPort failed\n");
    }

    return ret;
}

void close_reader()
{
	close_port();
}

#define SWAP_BYTES(a) ((a << 24) | ((a << 8) & 0x00FF0000) | ((a >> 8) & 0x0000FF00) | ((a >> 24) & 0x000000FF))
#define SWAP_WORDS(a) ((a << 16) | ((a >> 16) & 0x0000FFFF))

int iso15693_inventory(char* buf, int size)
{
	char tmp[512];
	char id[16];
	int received, i;
	int found = 0;

	if (buf && size) 
	{
		tmp[0] = 0;
		received = send_command("010B000304140401000000", tmp, 512);
//		printf("%s", tmp);

		for (i = 0; i < received; i++) 
		{
			if ((tmp[i] == '[') && (tmp[i+1] != ',') && (tmp[i+1] != ']') && (tmp[i+2] != ','))
			{
                                int j;
                                for (j = 0; j < sizeof(id); j++)                                                                             
                                {                                                                                                            
                                        if (!isxdigit(tmp[i+1+j]))                                                                           
                                        {                                                                                                    
                                                break;                                                                                       
                                        }                                                                                                    
                                }                                                                                                            
                                                                                                                                             
                            if (j == sizeof(id))                                                                                             
                            {                                                                                                                
                                unsigned int* ptmp = (unsigned int*)(&tmp[i+1]);                                                             
                                unsigned int* pid = (unsigned int*)id;                                                                       
                                pid[0] = SWAP_WORDS(ptmp[3]);                                                                                
                                pid[1] = SWAP_WORDS(ptmp[2]);                                                                                
                                pid[2] = SWAP_WORDS(ptmp[1]);                                                                                
                                pid[3] = SWAP_WORDS(ptmp[0]);                                                                                
                                                                                                                                             
                                // TODO: check buffer size                                                                                   
                                memcpy(buf + found*sizeof(id), id, sizeof(id));                                                              
                                i += sizeof(id)+1;                                                                                           
                                found++;                                                                                                     
                            }   
			}
		}
	}

	if (found > 0)
	{
		buf[found*sizeof(id)] = 0;
	}

	return found;
}

int main(void) 
{
	char buf[128];

	open_reader("/dev/ttyUSB0");

	while (1) 
	{
		buf[0] = 0;
		// send_command("010B000304140401000000", buf, 512);
		if (iso15693_inventory(buf, 128))
			printf("%s\n", buf);

		usleep(100000);
	}

	close_reader();

    return 0;
}


#ifdef _PYTHON
static PyObject*
rfid_open(PyObject* self, PyObject* args)
{
    char* port = NULL;
    int ret = -1;

    if (PyArg_ParseTuple(args, "s", &port))
    {
        // ret = open_reader("/dev/ttyUSB0");
        ret = open_reader(port);
    }
 
    return Py_BuildValue("i", ret);
}
 
static PyObject*
rfid_close(PyObject* self, PyObject* args)
{
    close_reader();

    return Py_BuildValue("i", 0);
}
 
static PyObject*
rfid_15693inventory(PyObject* self, PyObject* args)
{
	char buf[128];
	buf[0] = 0;
	if (iso15693_inventory(buf, 128))
	{
//		printf("%s\n", buf);
	}

    return Py_BuildValue("s", buf);
}
 
static PyMethodDef JogTekMethods[] = {
    {"rfid_open", rfid_open, METH_VARARGS, "Open reader."},
    {"rfid_close", rfid_close, METH_VARARGS, "Close reader."},
    {"rfid_15693inventory", rfid_15693inventory, METH_VARARGS, "ISO 15693 inventory."},
    {NULL, NULL, 0, NULL}
};
 
PyMODINIT_FUNC
initjogtek(void)
{
    (void) Py_InitModule("jogtek", JogTekMethods);
}
#endif
