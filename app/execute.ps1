$AssignEntry = "jar -ufe app\build\libs\app.jar org.example.App"
$Command = "java -jar app\build\libs\app.jar --ni m.java"
Invoke-Expression ($AssignEntry + " && " + $Command )
