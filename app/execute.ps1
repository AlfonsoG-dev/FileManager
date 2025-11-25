$AssignEntry = "jar -ufe build\libs\app.jar org.example.App"
$Command = "java -jar build\libs\app.jar --h"
Invoke-Expression ($AssignEntry + " && " + $Command )
