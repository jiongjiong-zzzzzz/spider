import frida_tools
import frida
process = frida.get_usb_device().attach('com.example.seccon2015.rock_paper_scissors')