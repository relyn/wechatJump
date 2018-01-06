cd "C:\Program Files\adb"
adb shell /system/bin/screencap -p /sdcard/screenshot.png
adb pull /sdcard/screenshot.png C:\Users\Relyn\Desktop
