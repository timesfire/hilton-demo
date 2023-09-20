1、 https://api.graphql.jobs/ 服务是不通的，用的 postman https://graphql.postman-echo.com/graphql 测试了一下demo
的通道是OK的

2、用的mock的数据跑通的整个流程

3、关于mock 数据中 company 字段的说明：
从截图来看，该字段是一个对象，要求中却当作数组来处理的，由于拿不到真实的数据，这里简单按照对象来处理