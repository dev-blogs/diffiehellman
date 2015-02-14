PATH_TO_PROJECT=`pwd`

# Удаляем каталог с предыдущим билдом
if test -d $PATH_TO_PROJECT/build;
then
    rm -rf $PATH_TO_PROJECT/build
fi
    
# Создаем новый каталог, куда будем помещать билд
mkdir $PATH_TO_PROJECT/build
    
# Компилируем проект в каталог build
# Для компиляции достаточно библиотеки spring-2.5.5.jar
javac -d build src/dev/blogs/diffiehellman/*.java

# Выполняем приложение
java -cp build dev.blogs.diffiehellman.Run
