# 基于Spring Boot的权限后台管理系统

### 1.使用技术栈：

 前端：bootstrap，HTML，CSS，JavaScript，Thymeleaf，JQuery，Gentelella Admin模板

后端：Spring Boot、Spring Data JPA、Spring Security、SpringAOP，Druid数据链接池

数据库：MySQL

调试工具：Postman调试后端接口()

### 2.开发环境：

IDEA2020.3，MySql5.7

### 3.开发背景：

此项目单纯是为了练手，且做一个以后能用来当轮子使用的后台管理的项目，并没有集成太多的功能，仅仅做了基本的CRUD以及图表，报表，安全权限等基础功能（后续功能可以直接添加）。由于我不太会Vue所以并没有采用现在流行的前后端分离的模式。但后端方面已经使用了RestFul风格的接口。

### 4.功能模块：

1.后台信息的查询功能：利用分页进行展示

2.公司信息的CRUD：添加的时候正则的判断。同时做一个侧边栏详细信息的显示，用颜色区别不同的信息，查询功能这块运用了JpaSpecificationExecutor通过集成Specification来进行多条件的查询，之所以不使用JPA自带的面向对象通过固定的方法名来实现查询方式的原因是，因为这种方法的局限性有点大，而且会造成超级长的方法名。

3.文件的上传功能：把文件保存在本地磁盘中，在数据库中保存路径。后期升级可以使用MogonDB作文文件服务器。

4.Echarts图表功能：list集合中的数据格式较为统一，为了统一，定义一个实体(不与数据库表对应)，该实体包括两个属性：name、value。利用ajax从远端获取数据，遍历反馈回的json数据根据不同类型图表对数据的要求，进行数据的组装，并赋值给echarts就行了

5.权限控制：通过Spring Security实现对后台管理系统登录人员的角色权限控制和安全控制。Security实际上是类似MVC里面的过滤器，通过过滤url来实现权限的管理。这里要注意一定Security里面是没有角色的概念的，角色只是后期为了方便区分而增加的一个概念。且权限对比的唯一依据是权限的名称而不是是否可以访问那些url，所以在定义数据库权限表的时候url是多余的。

6.权限功能的数据库表：在权限表中增加了一项父id和子id，没有采用之前那种方式仅仅通过一种id来区分权限角色，而是通过树形的结构来进行区分。每个父id权限还可以区分更多的权限

7.管理角色权限账号的CRUD：增加了一个专门负责去CRUD账号角色和权限的控制模块。

8.权限区分：通过前端Thymeleaf模板中的sec:hasAuthority()进行区分。不同的权限可以访问那些控件

9.日志功能：通过Spring AOP的机制来设计日志模块的功能。先通过自定义注解类和切面类再在需要添加操作日志的地方加上自定义日志注解。

10.报表功能：通过JasperReport报表来实现一个企业的报表功能，同时导入依赖POI和POI—OOXML协助处理Excela、建立了一个UtilJasperExport的类作为报表的工具类使用，主要负责把从数据库查询到的公司信息转化成JasperReport格式然后再转化成HTML输出在页面中，然后再可以吧JasperReport格式的信息转化成Excel的格式下载到本地。

### 5.开发过程中总结的问题

1.@Transactional //声明式事务管理。要么成功要么失败，利用Spring aop实现不影响其他事务的运行。常常用于Update相关操作，避免完全回滚。

2.同时在控制层中因为不是前后端分离的项目，所以没使用@RestController这种只放回Json数据的格式，而是使用了传统的@Controller返回数据且返回页面

3.Controller层：

**@Resource**：**默认byName **注入的方式，适用于接口只能有一个实现类，名称是@Service注解中标定的名称，如果通过 byName 的方式匹配不到，再按 byType 的方式去匹配。如@Resource(name="companyService")，这里的“companyService”是业务层实现类里面@Service注解中标定的名称。

**@Qualifier**：**byName **注入的方式，但名称是类名，适用于接口有多个实现类的场景，如@Qualifier(name="CompanyService")

**@Autowired**： **byType **注入方式，要求接口只能有一个实现类，Spring会按 byType的方式寻找接口的实现类，如果有多个实现类，Spring不知道要引入哪个类，自然就会报错

4.favicon.ico要放在resources（新建的）目录下

5.在日志模块中：先通过自定义注解类和切面类再在需要添加操作日志的地方加上自定义日志注解。（强调一下，日志表中的操作详情内容依赖于方法的返回数据，所以希望记录操作详情的方法一定要有返回数据，如果没有，则详情一列为空）将查询出来的日志记录在数据库中，同时增加了两个工具类负责UtilFilterPureEntity用来过滤实体中的非基本属性（如list,mangtomany等），得到只含基本属性的map对象。UtilCompareEntity用来比较两个实体的value值的差异，输出不一致的属性，主要用于日志中记录实体各项属性修改前和修改后的值。使用的时候在需要添加操作日志的地方加上自定义日志注解（强调一下，主要针对修改和删除，日志表中的操作详情内容依赖于方法的返回数据，所以希望记录操作详情的方法一定要有返回数据，如果没有，则详情一列为空）

6.使用Druid数据链接池的好处是拥有强大的功能，可以通过页面查看sql语句的性能。且在1.1.10之后的版本可以通过纯YML的形式去配置链接池

7.在配置Controller层的时候返回值的url值一定要去除前面的"/"，而mapping映射的url有无"/"均无所谓。

### 6. 使用操作：

管理员账号：admin  密码：123456  使用邮箱电话号码均可登录

Durid账号：admin   密码：123456

项目的部署：直接导入IDEA即可使用，ecplise也可以使用。

页面展示：
登录页面
![image](https://github.com/bedbanan/Spring-Boot-Web-Manamgement/blob/master/1.PNG)

