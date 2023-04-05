# Проблема!
Проект нормально работает если запускать из Idea.
При запуске из терминала командой:

java -jar moneytransferservice-0.0.1-SNAPSHOT.jar

не запускается и выдает сообщение:

Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2023-04-05T22:31:22.695+03:00 ERROR 5162 --- [           main] o.s.boot.SpringApplication               : Application run failed

org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'transferService': Lookup method resolution failed

Соответственно и не запускается и в Docker контейнере 