set "tomcat=C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps"
set "framework=Framework/src/java/etu1381/framework/"
javac -parameters -d Framework/build/web/WEB-INF/classes %framework%*.java %framework%modelview/*.java %framework%annotation/*.java %framework%init/*.java %framework%servlet/*.java %framework%file/*.java %framework%util/*.java 
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