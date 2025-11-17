$srcClases = "src\application\*.java src\application\opreation\*.java src\application\utils\*.java "
$libFiles = ""
$compile = "javac --release 23 -Xlint:all -Xdiags:verbose -d .\bin\ $srcClases"
$createJar = "jar -ufe FileManager.jar application.FileManager -C .\bin\ ."
$javaCommand = "java -jar FileManager.jar"
$runCommand = "$compile" + " && " + "$createJar" + " && " +"$javaCommand"
Invoke-Expression $runCommand 
