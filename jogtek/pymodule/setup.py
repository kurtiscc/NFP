from distutils.core import setup, Extension
 
module1 = Extension('jogtek', sources = ['jogtekmodule.c'])
 
setup (name = 'JogTekRFID',
        version = '1.0',
        description = 'JogTek RFID Driver',
        ext_modules = [module1])
