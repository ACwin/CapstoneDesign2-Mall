# [CapstoneDesign2-Mall](https://github.com/ACwin/CapstoneDesign2-Mall)



## 요약

사람들의 생활은 쇼핑을 떠날 수 없기 때문에 인터넷 쇼핑몰은 사람들에게 더욱 많은 편리를 가져다 줄 것이다. 하지만 고성능 쇼핑몰은 여전히 많은 건축 디자인을 필요로 하다. 본 쇼핑몰 사이트의 전후단 분리 아키텍처를 사용하면, 높은 동시성과 고가용성으로, 개발의 미학을 더욱 느낄 수 있다.

## 연구배경

**High concurrency**은 이제 주요 웹사이트가 연구해야 하는 중요한 과제 중 하나가 되었다. 그 이유는 매우 간단하다. 트래픽이 커짐에 따라 인터페이스 응답 시간 초과, GC 빈번, 데드록, 빅 데이터 저장 등 다양한 기술적 문제에 직면하다. 높은 동시성은 큰 트래픽을 의미하며, 트래픽의 충격에 저항하기 위해 기술적 수단을 사용해야 한다. 이러한 수단은 트래픽을 조작하는 것과 같다. 트래픽이 시스템에 의해 보다 안정적으로 처리되어 사용자에게 더 나은 경험을 제공한다. 우리가 흔히 볼 수 있는 하이 컨커런트 시나리오는 초당 수십만 개의 요청 초 킬 시스템, 매일 천만 개의 주문 시스템, 매일 억 개의 일일 작업 정보 흐름 시스템 등 모두 하이 컨커런트 시스템으로 분류할 수 있다.

<strong>High concurrency</strong>은 큰 트래픽을 의미하며, 트래픽의 충격에 저항하기 위해 기술적 수단을 사용해야 한다. 이러한 수단은 트래픽을 조작하는 것과 같으며, 트래픽을 시스템에 보다 안정적으로 처리하여 사용자에게 더 나은 경험을 제공할 수 있다.

그래서, 인터넷 서비스를 통해 완전한 웹사이트가 되어야 할 뿐만 아니라 우리가 개발한 웹사이트의 성능도 고려해야 한다고 생각한다.이것이야말로 좋은 개발자의 기본 소양이다.

## 연구목표

High concurrency은 high performance만 추구하는것을 의미하지 않는다. 이는 많은 사람들의 일면적인 리해이다.거시적 관점에서 고동시성 시스템 설계의 목표는 세 가지이다:고성능, 고가용성, 고확장성이다.

1. high performance:성능은 시스템의 병렬처리 능력을 구현합니다. 제한된 하드웨어 투자에서 성능 제고는 비용을 절약합니다.동시에 성능은 사용자 경험을 반영하는데 응답 시간이 각각 100 밀리초와 1초여서 사용자의 느낌이 완전히 다르다.

2. High availability:시스템이 정상적으로 서비스를 받을 수 있는 시간을 나타낸다.1년 내내 가동이 중단되지 않고 고장이 없다.다른 하나는 사흘이 멀다하고 사고와 다운이 발생하니, 사용자들은 전자를 선택할 것이다.이밖에 만약 시스템이 90% 밖에 사용할수 없다 해도 업무에 큰 지장을 주게 된다.

3. High scalability:시스템 확장 능력을 나타낸다. 트래픽이 피크 때 단시간 내에 확장을 완성할 수 있고, 더욱 평온하게 피크 트래픽을 접수한다. 예를 들면 Alibaba의 더블 11 활동 등.

이 세 가지 목표는 서로 관련되어 있고, 심지어 서로 영향을 주기도 하기 때문에 종합적으로 고려되어야 한다.



## 시스템 설계
![系统架构图2](https://user-images.githubusercontent.com/42907149/204136231-fd04c2cd-c271-4bbd-ab37-f92e461843b4.png)

### Redis + MQ 구성도

![img](https://segmentfault.com/img/remote/1460000021621566)

1. **Preheat cache**: Second skill products are generally highly effective, and one second skill activity does not last long. When setting up a second skill activity in the background, the inventory for that product is stored directly in the ready. However, you must set the expiration time when you set the cache.

2. **Reduce request flow:** When users enter detailed information about skill products, all follow-up actions must be inventoryed, skill qualification checked.

3. **Deduct the Redis inventory:** When a user places an order from a thread detail to a billing page, it adds a distributed lock to prevent the user from duplicating the request. If a follow-up check is passed, the ready inventory is deducted. Ensure thread safety through Redis and ensure that goods are not oversold. However, the db inventory is not deducted.

4. **db Inventory Deduction:** The user will confirm the success of the recipient payment and deduct the db inventory in an optimistic lockout format.Since stock acquisition leads to subsequent payment logic, traffic in stock payment logic is generally very small, and if db stock reduction fails through optimistic locking, the message is retried to ensure consistency between Redis and db stocks.

5. **Return Ready Inventory:** You may not pay if you have obtained inventory; if you cancel an order or if an order does not pay, you will receive cancellation news and return the ready stock.

6. **Return Redis and db inventory:** Redis and db inventory is user.If you request a return after making a provisional payment, it will be returned. You can hear the transaction news or process it directly through the direct exchange interface. Because the refund scenario is small and there is not much traffic.

   This approach is relatively simple, especially when flow control is needed upstream when there is a particularly high flow rate, considering whether cache penetration and cache avalanche problems can occur Generally enough.

## Features

[![jdkversions](https://camo.githubusercontent.com/027259a36d0c22143fcc22e83ab06aaf49f9309fd8183e03c90b52ccfe226532/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4a6176612d312e372532422d79656c6c6f772e737667)](https://github.com/ACwin/CapstoneDesign2-Mall/blob/main) [![vueversions](https://camo.githubusercontent.com/7f4639c179358bdf687e581f05674168d81695502a128c8b74a2a0bc38f007e2/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f7675652e6a732d322e322e782d627269676874677265656e2e737667)](https://github.com/ACwin/CapstoneDesign2-Mall/blob/main) [![nginx 1.21.6](https://camo.githubusercontent.com/e616a04e3805b9a0df6816fb21cc91c3cc1d2fd6329859f899454a8750655d13/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6e67696e782d312e32312e362d627269676874677265656e2e737667)](https://camo.githubusercontent.com/e616a04e3805b9a0df6816fb21cc91c3cc1d2fd6329859f899454a8750655d13/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6e67696e782d312e32312e362d627269676874677265656e2e737667) [![mysql](https://camo.githubusercontent.com/5429cf14d5971f922fc47ae3596cc994b4a0ed3507d94994dd1307d7c57e6f6c/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6d7973716c2d352e372d627269676874677265656e2e737667)](https://camo.githubusercontent.com/5429cf14d5971f922fc47ae3596cc994b4a0ed3507d94994dd1307d7c57e6f6c/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6d7973716c2d352e372d627269676874677265656e2e737667) [![mybatis](https://camo.githubusercontent.com/0c90b66f36d5a869901eec585425669d5a04c0df85d23e9b0cc5b3dc773c6bef/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6d7962617469732d322e322d627269676874677265656e2e737667)](https://camo.githubusercontent.com/0c90b66f36d5a869901eec585425669d5a04c0df85d23e9b0cc5b3dc773c6bef/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6d7962617469732d322e322d627269676874677265656e2e737667)

### 1. User Modules:

[![image](https://user-images.githubusercontent.com/42907149/172845395-c7f0a57f-5d80-41e1-a657-621943ae37d9.png)](https://user-images.githubusercontent.com/42907149/172845395-c7f0a57f-5d80-41e1-a657-621943ae37d9.png)

### 2. Commodity Classification Module:：

- 2.1 The settings for categorical data
- 2.2 Affiliation and association of goods with categories

### 3. Commodity module:

- 3.1 Image Upload
- 3.2 Customize the static resource mapping directory
- 3.3 Updates and additions to products
- 3.4 Batch loading and unloading
- 3.5 Product List: Search function

### 4.Summary Commodity Module:

[![image](https://user-images.githubusercontent.com/42907149/172758196-bffc6253-05ff-48e1-a09a-e82f5c80e3df.png)](https://user-images.githubusercontent.com/42907149/172758196-bffc6253-05ff-48e1-a09a-e82f5c80e3df.png)

### 5. Order module development

- 5.1 Generate orders - the user places an order
- 5.2 Generate orders - the user places an order
- 5.3 Generate orders - the user places an order
- 5.4 pay

[![image](https://user-images.githubusercontent.com/42907149/172758819-917f7f1b-f66f-44bc-ad78-493de73636b2.png)](https://user-images.githubusercontent.com/42907149/172758819-917f7f1b-f66f-44bc-ad78-493de73636b2.png)

## Technology selection

#### Back-end technology

| Technology           | Instruction                                | Official website                                    |
| -------------------- | ------------------------------------------ | --------------------------------------------------- |
| SpringBoot           | Container + MVC framework                  | https://spring.io/projects/spring-boot              |
| SpringSecurity       | Authentication and authorization framework | https://spring.io/projects/spring-security          |
| MyBatis              | ORM framework                              | http://www.mybatis.org/mybatis-3/zh/index.html      |
| MyBatisGenerator     | Data-layer code generation                 | http://www.mybatis.org/generator/index.html         |
| Elasticsearch        | search engine                              | https://github.com/elastic/elasticsearch            |
| RabbitMQ             | Message Queuing                            | https://www.rabbitmq.com/                           |
| Redis                | Distributed cache                          | https://redis.io/                                   |
| MySql                | database                                   | [https://www.mongodb.com](https://www.mongodb.com/) |
| LogStash             | Log collection tool                        | https://github.com/elastic/logstash                 |
| Kibana               | Log visualization tool                     | https://github.com/elastic/kibana                   |
| Nginx                | Static resource server                     | https://www.nginx.com/                              |
| Docker               | Apply the container engine                 | [https://www.docker.com](https://www.docker.com/)   |
| Jenkins              | Automated deployment tools                 | https://github.com/jenkinsci/jenkins                |
| Druid                | Database connection pooling                | https://github.com/alibaba/druid                    |
| OSS                  | Object storage                             | https://github.com/aliyun/aliyun-oss-java-sdk       |
| MinIO                | Object storage                             | https://github.com/minio/minio                      |
| JWT                  | JWT login support                          | https://github.com/jwtk/jjwt                        |
| Lombok               | Simplified object encapsulation tool       | https://github.com/rzwitserloot/lombok              |
| Hutool               | Java Tools Class Library                   | https://github.com/looly/hutool                     |
| PageHelper           | MyBatis Physical paging plug-in            | http://git.oschina.net/free/Mybatis_PageHelper      |
| Swagger-UI           | Document generation tools                  | https://github.com/swagger-api/swagger-ui           |
| Hibernator-Validator | Validation framework                       | http://hibernate.org/validator                      |

#### Front-end technology

| Technology | Instruction                         | Official website                                      |
| ---------- | ----------------------------------- | ----------------------------------------------------- |
| Vue        | Front-end framework                 | https://vuejs.org/                                    |
| Vue-router | Routing framework                   | https://router.vuejs.org/                             |
| Vuex       | Global state management framework   | https://vuex.vuejs.org/                               |
| Element    | Front-end UI framework              | [https://element.eleme.io](https://element.eleme.io/) |
| Axios      | Front-end HTTP framework            | https://github.com/axios/axios                        |
| v-charts   | Charting framework based on Echarts | https://v-charts.js.org/                              |
| Js-cookie  | Cookie management tool              | https://github.com/js-cookie/js-cookie                |
| nprogress  | Progress bar control                | https://github.com/rstacruz/nprogress                 |

## Environment setup

### Development tools

| Tool          | Instruction                  | Official website                                      |
| ------------- | ---------------------------- | ----------------------------------------------------- |
| IDEA          | Develop the IDE              | https://www.jetbrains.com/idea/download               |
| RedisDesktop  | redis client connection tool | https://github.com/qishibo/AnotherRedisDesktopManager |
| SwitchHosts   | Local host management        | https://oldj.github.io/SwitchHosts/                   |
| X-shell       | Linux remote connection tool | http://www.netsarang.com/download/software.html       |
| Navicat       | Mysql client connection tool | http://www.formysql.com/xiazai.html                   |
| PowerDesigner | Database design tools        | http://powerdesigner.de/                              |
| Axure         | Prototyping tools            | https://www.axure.com/                                |
| MindMaster    | Mind Map Design Tools        | http://www.edrawsoft.cn/mindmaster                    |
| ScreenToGif   | Gif recording tool           | https://www.screentogif.com/                          |
| ProcessOn     | Flowcharting tools           | https://www.processon.com/                            |
| PicPick       | Image processing tools       | https://picpick.app/zh/                               |
| Snipaste      | Screenshot tool              | https://www.snipaste.com/                             |
| Postman       | API interface debugging tool | https://www.postman.com/                              |
| Typora        | Markdown Editor              | https://typora.io/                                    |

### Development environment

| Tool          | version number | Download                                                     |
| ------------- | -------------- | ------------------------------------------------------------ |
| JDK           | 1.8            | https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html |
| Mysql         | 5.7            | https://www.mysql.com/                                       |
| Redis         | 6.x            | https://redis.io/download                                    |
| Mysql         | 5.7            | https://www.mongodb.com/download-center                      |
| RocketMQ      | 3.7.14         | http://www.rabbitmq.com/download.html                        |
| Nginx         | 1.10           | http://nginx.org/en/download.html                            |
| Elasticsearch | 7.6.2          | https://www.elastic.co/downloads/elasticsearch               |
| Logstash      | 7.6.2          | https://www.elastic.co/cn/downloads/logstash                 |
| Kibana        | 7.6.2          | https://www.elastic.co/cn/downloads/kibana                   |

------

## 프로그램 및 모듈 설치


Project Link: [[CapstoneDesign2-Mall](http://119.91.139.92:8085/#/mall)]

**Front-end system:**
<img width="1726" alt="image" src="https://user-images.githubusercontent.com/42907149/204093673-7245e27f-95c9-4434-8fdf-8943b9d9368d.png">
<img width="1721" alt="image" src="https://user-images.githubusercontent.com/42907149/204095415-7c7d224d-3e65-4d61-b098-3e079f38a88a.png">
<img width="1717" alt="image" src="https://user-images.githubusercontent.com/42907149/204095239-73eccbcf-16ff-4635-8cb5-e149d42901e7.png">
<img width="1337" alt="image" src="https://user-images.githubusercontent.com/42907149/204095431-f6718b29-cd2b-4615-b76e-f37d809be5d3.png">

<img width="1720" alt="image" src="https://user-images.githubusercontent.com/42907149/204095260-756c4dd9-0941-4e90-a534-14cf0235279b.png">
<img width="1721" alt="image" src="https://user-images.githubusercontent.com/42907149/204095315-faf0b98b-1a44-4e54-93b0-4604b4b06b59.png">



**Backend management system:**

<img width="1725" alt="image" src="https://user-images.githubusercontent.com/42907149/204095335-f1c867ef-3342-4cc5-b829-37928efe12a9.png">
<img width="1728" alt="image" src="https://user-images.githubusercontent.com/42907149/204095340-cc42d343-584c-4bb8-a511-7c8d5b5331f7.png">
<img width="1728" alt="image" src="https://user-images.githubusercontent.com/42907149/204095348-7a4d9e7f-d873-4d0f-9a34-a61b19914d86.png">

### front

[![image](https://user-images.githubusercontent.com/42907149/204093525-644de554-87f2-45bf-9cc2-540886def8e3.png)](https://user-images.githubusercontent.com/42907149/204093525-644de554-87f2-45bf-9cc2-540886def8e3.png)

## Project setup

```
npm install
```

### Compiles and hot-reloads for development

```
npm run serve
```

### Compiles and minifies for production

```
npm run build
```

### Lints and fixes files

```
npm run lint
```

### Customize configuration

See [Configuration Reference](https://cli.vuejs.org/config/).

### Contact

- YANG - [@ACwin](https://github.com/ACwin) - [email@uniceyang0116@gmail.com](mailto:emailuniceyang0116@gmail.com)

Project Link: [[https://github.com/ACwin/CapstoneDesign2-Mall](https://github.com/ACwin/CapstoneDesign2-Mall.git)]






