#### 1.Lambda 表达式 略
#### 2.Stream
**Stream和集合的区别**：

stream：stream是只计算当前需要的数据，在迭代过程中，stream是放在内部迭代的，集合的迭代是放在外部。在外部迭代就会需要自己解决管理并行的问题。   

![img](D:/%E4%B8%AD%E6%96%87%E5%AE%89%E8%A3%85%E8%B7%AF%E5%BE%84/%E6%9C%89%E9%81%93%E4%BA%91%E7%AC%94%E8%AE%B0/%E7%AC%94%E8%AE%B0%E4%BF%9D%E5%AD%98%E5%9C%B0%E5%9D%80/qq031651CFD1211506605F887E77625509/30d9ea44412743a292dc206e9b9ecd4d/clipboard.png)

集合：集合是一次计算所有的值，Stream的流只消费一次

#### 3. 流操作
连接起来的流操作称为中间操作，关闭流的操作称为终端操作。
```
List<String> collect = list.stream()
        .filter(e -> e.getId() > 2)  // 中间操作
        .sorted(comparing(YxUser::getCreateTime)) // 中间操作
        .map(YxUser::getUsername) // 中间操作
        .collect(Collectors.toList()); // 终端操作
```



**中间操作和终端操作的区别**：

**中间操作**：如filter、sort、map等中间操作都会返回一个另外一个流。这让多个操作可以连接起来形成一个查询。最重要的是：除非流水线上有一个终端操作，不然中间操作不会做任何处理，因为中间操作都可以合并起来，一起在终端操作一次性全部处理。
**终端操作**：都会从流的流水线产生结果，他的结果不是流。

![img](D:/%E4%B8%AD%E6%96%87%E5%AE%89%E8%A3%85%E8%B7%AF%E5%BE%84/%E6%9C%89%E9%81%93%E4%BA%91%E7%AC%94%E8%AE%B0/%E7%AC%94%E8%AE%B0%E4%BF%9D%E5%AD%98%E5%9C%B0%E5%9D%80/qq031651CFD1211506605F887E77625509/040cdb0d8b6c456691267a921cc74c0c/clipboard.png)

使用流一般包括三件事：①：一个数据源执行一个查询，②一个中间操作链，行程一条流的流水线，③：一个终端操作，执行流水线，生成最终结果。


#### 4.使用流
List<Integer> integerList =Arrays.asList(1,2,2,2,2,2,4,5,6,7,8);

**筛选**：谓词筛选filter 
```
 List<String> collect = list.stream()
        .filter(e -> e.getId() > 2)  //谓词筛选
        .collect(Collectors.toList()); // 终端操作    
```

![img](D:/%E4%B8%AD%E6%96%87%E5%AE%89%E8%A3%85%E8%B7%AF%E5%BE%84/%E6%9C%89%E9%81%93%E4%BA%91%E7%AC%94%E8%AE%B0/%E7%AC%94%E8%AE%B0%E4%BF%9D%E5%AD%98%E5%9C%B0%E5%9D%80/qq031651CFD1211506605F887E77625509/c34d660d58b14e449bf36944ebd36ed1/clipboard.png)

distinct顾名思义**：去掉重复的。

```
integerList.stream()
           .filter(i->i%2==0)
           .distinct()
           .forEach(System.out::println);
           //将输出所有偶数并且没有重复的
```

**limit**：返回前N个数据，类似mysql的limit上。
```
integerList.stream()
        .sorted()
        .limit(2)
        .forEach(System.out::println);
        排序后将输出前两个
```


**skip**：过滤掉前n个元素。
```
integerList.stream()
        .sorted()
        .skip(2)
        .limit(2)
        .forEach(System.out::println); 
        //排序后，先过滤前两个，在输出前两个。实际输出的是第3,4两个。
```

**映射**：

map:一般的用法：map就是取其中的一列
```
List<YxUser> list = Arrays.asList(
        new YxUser(1,"yanxgin","222","823721670@qq.com"),
        new YxUser(2,"12","222","823721670@qq.com"),
        new YxUser(3,"yan34xgin","222","823721670@qq.com"),
        new YxUser(4,"56","222","823721670@qq.com"),
        new YxUser(5,"78","222","823721670@qq.com"),
        new YxUser(6,"90","222","823721670@qq.com"),
        new YxUser(7,"666","222","823721670@qq.com")
 );

List<String> collect = list.stream()
        .filter(e -> e.getId() > 2)  // 中间操作
        .map(YxUser::getUsername) // 中间操作
        .collect(Collectors.toList()); // 终端操作
        将会返回username这一列
```

> map(Arrays::Stream)和flatMap(Arrays::Stream)的区别：前者是将数据转换成一个单独的流。
> 后者是将把流中的每个值都换成另外一个流，典型的列子是怎么统计一句英文句子中不同的字符。

**匹配**

anyMatch表示数据集中是不是有一个元素能够匹配给定的谓词

allMatch 表示流中的元素是否都能够匹配给定的谓词

noneMatch 表示流中没有匹配改给定的谓词

**查找**

findAny方法表示返回当前流中的任意元素
```
Optional<YxUser> any = list.stream()
        .filter(e -> e.getId() > 5)
        .findAny();
```
> Optional<T>：是一个容器类，表示一个值存在还是不存在，避免findAny找不到值的时候导致null的情况
> > isPresent ：表示optional包含值的时候返回true，反之false
> > >ifPresent(Consumer<T> t) ：表示存在时，执行存在的代码块
> > >
> > >> T get()会在值存在时返回值，否则抛出一个NoSuchElement异常。T orElse(T other)会在值存在时返回值，否则返回一个默认值


**查找第一个元素 findFirst**

#### 5.归约
**reduce**:首先要有一个初始值，还有第二个参数是执行规约的规则
```
List<Integer> integerList = Arrays.asList(1, 2, 2, 2, 2, 2, 4, 5, 6, 7, 8);
Integer reduce = integerList.stream()
        .reduce(0, (x, y) -> x + y);
Integer reduce = integerList.stream()
        .reduce(0, Integer::sum);
        这两个是一样的 还有Integer::MAX和MIN
```

![img](D:/%E4%B8%AD%E6%96%87%E5%AE%89%E8%A3%85%E8%B7%AF%E5%BE%84/%E6%9C%89%E9%81%93%E4%BA%91%E7%AC%94%E8%AE%B0/%E7%AC%94%E8%AE%B0%E4%BF%9D%E5%AD%98%E5%9C%B0%E5%9D%80/qq031651CFD1211506605F887E77625509/daac4c5552d64efda753ce953e170668/clipboard.png)

#### 6.数值流

**收集器使用groupingBy**：通过用户的用户名进行分组如下
```
Map<String, List<YxUser>> collect = list.stream()
        .collect(groupingBy(YxUser::getUsername));
//多级分组
// 首先按照性别分组，然后按照id分组。
Map<Integer, Map<String, List<YxUser>>> collect = list.stream()
.collect(groupingBy(YxUser::getSex, // 一级分类函数
groupingBy(e -> { // 二级函数
    if (e.getId() > 5) return "hight";
    else if (e.getId() < 4) return "small";
    else return "midle";
})));
// 按照子组收集数据
Map<Integer, Long> collect = list.stream()
        .collect(groupingBy(YxUser::getSex, counting()));
/**
* counting 可以换成maxBy、minBy
*/
```

如果是自己写的话，会嵌套多层循环，多级分组那么将会更难维护。
Collectors.maxBy和Collectors.minBy在collect中使用，参数是自定义的Comparator

```
Comparator<YxUser> comparator=Comparator.comparingInt(YxUser::getId);
Optional<YxUser> collect = list.stream()
        .collect(minBy(comparator));
// 使用reducing
Optional<YxUser> mostCalorieDish = list.stream().
collect(reducing( (d1, d2) -> d1.getId() < d2.getId() ? d1 : d2));
```

**summingInt**，在collect中计算总和。
```
Integer collect = list.stream().collect(summingInt(YxUser::getId));
// 如果使用reducing
int totalCalories = list.stream().
collect(reducing( 0, //初始值
YxUser::getId,//转换函数
Integer::sum);//累积函数
//第一个参数是归约操作的起始值，也是流中没有元素时的返回值，所以很显然对于数值和而言0是一个合适的值。
//第二个参数就是你在6.2.2节中使用的函数，将菜肴转换成一个表示其所含热量的int。
//第三个参数是一个BinaryOperator，将两个项目累积成一个同类型的值。这里它就是对两个int求和
```

还有类似的函数：averagingInt计算平均值
> 但是还可以通过summarizingInt可以一次性得到：对应的最大值、最小值、平均值、和、数量等信息，可以通过getter获取
>
> ![img](D:/%E4%B8%AD%E6%96%87%E5%AE%89%E8%A3%85%E8%B7%AF%E5%BE%84/%E6%9C%89%E9%81%93%E4%BA%91%E7%AC%94%E8%AE%B0/%E7%AC%94%E8%AE%B0%E4%BF%9D%E5%AD%98%E5%9C%B0%E5%9D%80/qq031651CFD1211506605F887E77625509/b61778caa98d4ec2ac87b038a02952c6/clipboard.png)

**joining连接字符串**：
```
String collect1 = list.stream().map(YxUser::getUsername).collect(joining());
System.out.println("collect1:" + collect1);
// 添加分割符
String collect2 = list.stream().map(YxUser::getUsername).collect(joining(", "));
System.out.println("collect2:" + collect2);
```

输出效果：

![img](D:/%E4%B8%AD%E6%96%87%E5%AE%89%E8%A3%85%E8%B7%AF%E5%BE%84/%E6%9C%89%E9%81%93%E4%BA%91%E7%AC%94%E8%AE%B0/%E7%AC%94%E8%AE%B0%E4%BF%9D%E5%AD%98%E5%9C%B0%E5%9D%80/qq031651CFD1211506605F887E77625509/879f3bdfcfa946c28ccc0ce2e79537b5/clipboard.png)

**求和的几种形式**：
```
list.stream().mapToInt(YxUser::getId).sum();

list.stream().map(YxUser::getId).reduce(Integer::sum).get();

list.stream().collect(reducing(0, YxUser::getId, Integer::sum));

list.stream().collect(reducing(0, YxUser::getId, (x, y) -> x + y));
```

**字符串拼接的几种形式**
```
list.stream().map(YxUser::getUsername).collect(reducing((s1,s2)->s1+s2)).get();

list.stream().collect(reducing("",YxUser::getUsername,(s1,s2)->s1+s2));

String collect2 = list.stream().map(YxUser::getUsername)
.collect(joining(", "));
// 从性能上考虑，建议使用joining
```


**partitioningBy分区函数**：

返回的主键是 boolean类型，只有true和false两种情况。分区其实就是分组的一种特殊情况。
```
Map<Boolean, List<YxUser>> collect = list.stream()
.collect(partitioningBy(YxUser::isX));
System.out.println("collect: " + collect);
```

**函数大全**

![img](D:/%E4%B8%AD%E6%96%87%E5%AE%89%E8%A3%85%E8%B7%AF%E5%BE%84/%E6%9C%89%E9%81%93%E4%BA%91%E7%AC%94%E8%AE%B0/%E7%AC%94%E8%AE%B0%E4%BF%9D%E5%AD%98%E5%9C%B0%E5%9D%80/qq031651CFD1211506605F887E77625509/f13a96d528824eadb1c397b425941275/clipboard.png)

![img](D:/%E4%B8%AD%E6%96%87%E5%AE%89%E8%A3%85%E8%B7%AF%E5%BE%84/%E6%9C%89%E9%81%93%E4%BA%91%E7%AC%94%E8%AE%B0/%E7%AC%94%E8%AE%B0%E4%BF%9D%E5%AD%98%E5%9C%B0%E5%9D%80/qq031651CFD1211506605F887E77625509/e8c4b005d5154eb29ee921e24f67db3b/clipboard.png)

