# Explore With Me
## Идея проекта 
Свободное время — ценный ресурс. Ежедневно мы планируем, как его потратить — куда и с кем сходить. Сложнее всего в таком планировании поиск информации и переговоры. 
Нужно учесть много деталей: какие намечаются мероприятия, свободны ли в этот момент друзья, как всех пригласить и где собраться.
Приложение, которое вы будете создавать, — афиша. В этой афише можно предложить какое-либо событие от выставки до похода в кино и собрать компанию для участия в нём.
## Стек проекта
Java 11, Spring Boot, Lombok, PostgreSQL, Spring Data JPA, Hibernate, H2, mapstruct, QueryDSL.
## Техническое задание
<details>
 <summary>Этап 1. Сервис статистики</summary>

 Первый этап — реализация сервиса статистики. Его функционал достаточно прост и ограничен, поэтому начать с него будет лучше всего. 
 Реализация сервиса статистики позволит вам разобраться со спецификацией API и основными требованиями ТЗ, а также  подготовить сборку проекта.
 
 ### На первом этапе необходимо:
 1. Реализовать сервис статистики в соответствии со спецификацией: ewm-stats-service.json.
 2. Реализовать HTTP-клиент для работы с сервисом статистики.
 3. Подготовить сборку проекта.
 4. Определится с тематикой дополнительной функциональности, которую вы будете реализовывать.
 
 ### Базовые требования
 Разработка должна вестись в публичном репозитории, созданном на основе шаблона. 
 Весь код первого этапа разместите в отдельной ветке с именем stat_svc.
 ### Что будет проверяться
 * Работающая сборка проекта:
   * проект компилируется без ошибок;
   * сервис статистики успешно запускается в докер-контейнере;
   * экземпляр PostgreSQL для сервиса статистики успешно запускается в докер-контейнере.
 * Корректная работа сервиса статистики:
   * все эндпоинты отрабатывают в соответствии со спецификацией;
   * данные успешно сохраняются и выгружаются из базы данных;
   * реализован HTTP-клиент сервиса статистики.
 ### Как подготовить сборку проекта
 1. Учитывайте многомодульность.

Приложение дипломного проекта должно состоять из двух отдельно запускаемых сервисов — в контексте сборки проекта при помощи Maven это означает, что проект будет многомодульным. Но это ещё не всё. Сами сервисы можно также разбить на подмодули. 

Сервис статистики должен состоять из HTTP-сервиса и HTTP-клиента. Это значит, что модуль статистики можно разделить на два подмодуля. 

Механизм взаимодействия сервиса и клиента предполагает, что они будут использовать одни и те же объекты для запросов и ответов. Исходя из этого, можно выделить еще один подмодуль, в котором будут размещены общие классы DTO.

 2. Поработайте с файлами.
  * модули основного сервиса и сервиса статистики должны содержать dockerfile;
  * в корне проекта должен быть создан файл docker-compose.yml, описывающий запуск контейнеров с сервисами проекта и базами данных для них.
  * файл pom.xml, описывающий сборку основного сервиса, на данном этапе должен содержать только указание на родительский модуль и идентификатор артефакта.
 3. Проверьте обязательные зависимости.
Одной из обязательных зависимостей в каждом из сервисов должен быть Spring Boot Actuator.

</details>
<details>
 <summary>Этап 2. Основной сервис</summary>

 ## Базовые требования
 Реализация должна вестись в отдельной ветке с именем main_svc. Эта ветка должна основываться на ветке main в которую слиты изменения предыдущего этапа.
 
 ## Что будет проверяться
 
   * Работающая сборка проекта:
     * проект компилируется без ошибок;
     * основной сервис и сервис статистики успешно запускаются в Docker-контейнерах;
     * для каждого сервиса запускается свой экземпляр PostgreSQL в Docker-контейнере.
  * Корректная работа основного сервиса:
     * все эндпоинты отрабатывают в соответствии со спецификацией;
     * данные успешно сохраняются и выгружаются из базы данных;
     * основной сервис и сервис статистики корректно взаимодействуют;
     * реализация работы с данными не производит лишней нагрузки на базу данных.
</details>
<details>
 <summary>Этап 3. Дополнительная функциональность</summary>

 Вы уже спроектировали полноценное приложение — и бóльшая часть дипломной работы позади. Поздравляем!
 Осталось последнее задание — реализация выбранной вами дополнительной функциональности. На этом этапе вам предстоит, во-первых, реализовать саму функциональность, а также написать базовые Postman-тесты, которые будут проверять её работоспособность.
 
 ## Базовые требования
 
 Реализация должна вестись в отдельной ветке с именем feature_NAME, где NAME — краткое название дополнительной функциональности:
  * comments — комментарии к событиям;
  * subscriptions — подписки на других пользователей;
  * rating_events — лайки/дизлайки, рейтинг мероприятий;
  * location_processing — администрирование локаций;
  * moderation_enhancement — модерация событий администратором;

 ## Что будет проверяться
 
 * Работоспособность сервисов, реализованных на предыдущих этапах.
 * Наличие базовых Postman-тестов. Они должны проверять коды ответов спроектированных вами эндпоинтов в рамках реализации выбранной функциональности.
 * Полнота и корректность реализации выбранной функциональности.

 ## Путь к Postman-коллекции
Вам нужно экспортировать Postman-коллекцию и сохранить её в папке Postman. Сам файл должен называться feature.json. Путь к файлу в репозитории должен быть postman/feature.json.

 ## Когда все готово
После того как диплом будет готов, его нужно сдать архивом. Это необходимо, чтобы сформировать цифровой след — он будет олицетворять факт успешно завершенного обучения. Для этого сделайте следующее. 
 1. В Readme.md добавьте ссылку на пул-реквест, открытый из ветки feature_NAME в main. Прикладывайте ссылку именно на пул-реквест, а не на репозиторий, чтобы ревьюер мог оставить комментарии.
 2. В аккаунте GitHub выберите ветку — feature_NAME.
 3. Нажмите на кнопку “Code” и выберите “Download ZIP”.

Не забудьте обновлять архив перед каждой проверкой. Если код в архиве и репозитории будет отличаться, работа будет отклонена от проверки. Как только работа будет зачтена, нажмите кнопку “Merge”, чтобы применить изменения из ветки с фичей в main. 
Поздравляем! Вы проделали огромную работу! Вас ждёт ваше последнее ревью в Практикуме. После него начнётся ваш самостоятельный путь в профессии Java-разработчика! 
</details>
