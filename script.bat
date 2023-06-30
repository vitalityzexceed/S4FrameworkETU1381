set "tomcat=C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps"
javac -parameters -d Framework/build/web/WEB-INF/classes Framework/src/java/etu1381/framework/*.java Framework/src/java/etu1381/framework/modelview/*.java Framework/src/java/etu1381/framework/annotation/*.java Framework/src/java/etu1381/framework/init/*.java Framework/src/java/etu1381/framework/servlet/*.java Framework/src/java/etu1381/framework/file/*.java
cd Framework/build/web/WEB-INF/classes/
jar cvf ../../../../../TestFramework/WEB-INF/lib/framework.jar etu1381
cd ../../../../../
javac -parameters -d TestFramework/WEB-INF/classes -cp "TestFramework/WEB-INF/lib/framework.jar" TestFramework/src/java/etu1381/framework/model/*.java
robocopy "TestFramework" "FrameworkToDeploy" /E /XD "src"
cd FrameworkToDeploy
jar -cvf ../FrameworkToDeploy.war *
cd ../
copy "FrameworkToDeploy.war" "%tomcat%"
del /F FrameworkToDeploy.war
rmdir /S /Q FrameworkToDeploy