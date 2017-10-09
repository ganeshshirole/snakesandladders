@echo Clean a Android Studio project ready for importing and zipping pure code
@echo Modify this file to meet project requirements
pause
@echo Remove Gradle code, added back in on import
rmdir .gradle /s /q
@echo Remove IDE files
rmdir .idea /s /q
del *.iml /f /s
del local.properties
@echo Remove build folders, will be recreated
rmdir build /s /q
rmdir app\build /s /q
@echo Remove Gradle Wrapper, will be added back in
rmdir gradle /s /q
@echo Remove Git ignore files
del .gitignore /f /s
@echo Remove other Gradle files
del gradle.properties
del gradle?.*
@echo Remove libs folder
rmdir app\libs /s /q
@echo Remove ProGuard rules
del app\proguard-rules.pro /f
@echo Remove test code
rmdir app\src\androidTest /s /q
rmdir app\src\test /s /q
@echo Clear Read-only attributes
attrib -R *.* /s
@echo Do not forget to edit build.gradle in the app directory