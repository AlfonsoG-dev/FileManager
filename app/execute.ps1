$AssignEntry = "jar -ufe app\build\libs\app.jar org.example.App"
$Command = "java -jar app\build\libs\app.jar --cp app\README.md app\execute.ps1 To app\docs app\lib"
Invoke-Expression ($AssignEntry + " && " + $Command )
