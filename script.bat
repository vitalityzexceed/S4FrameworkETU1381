javac -d Framework/build/web/WEB-INF/classes Framework/src/java/etu1381/framework/*.java Framework/src/java/etu1381/framework/annotation/*.java Framework/src/java/etu1381/framework/init/*.java Framework/src/java/etu1381/framework/servlet/*.java
cd Framework/build/web/WEB-INF/classes/
jar cvf ../../../../../TestFramework/WEB-INF/lib/framework.jar etu1381
cd ../../../../../
javac -d TestFramework/WEB-INF/classes -cp "TestFramework/WEB-INF/lib/framework.jar" TestFramework/src/java/etu1381/framework/model/*.java
cd TestFramework
jar -cvf ../TestFramework.war *