## effective java

- 代码 清晰性和简洁性最为重要：组件的用户永远也不应该被其行为所迷惑 。

-  代码应该被重用，而不是被拷贝 

-  用静态工厂方法代替构造器

  > 1. 静态工厂方法与构造器不同的第一大优势在于，它们有名称 。
  >
  >    ​	因为一个类只有一个带有指定签名的构造器，开发一般都是提供多个构造器避开这个原则，但是对于是使用者来说，往往不知道调取那个构造器。由于静态工厂方法有名称，所以他们不受上述限制。
  >
  > 2.  不必在每次调用它们的时候都创建一个新对象。
  >
  >    因为是静态的，在jvm启动的时候就把实例缓存起来了的，可以进行重复利用。类似于享元模式。如果程序经常请求创建相同的对象，都应该尽可能的避免
  >
  > 3.  它们可以返回原返回类型的任何子类型的对象 
  >
  > 4.  所返回的对象的类可以随着每次调用而发生变化，这取决于静态工厂方法的参数值 

  如下是一个全局响应的模板类例子：

  ```java
  @Data
  public class DataCommRes<T> implements Serializable {
  
      protected String errormsg;      //一般是错误信息
      protected Boolean result = true;        //返回结果是否正常
      protected String msg;       //返回信息
      protected Integer resultcode = 200;     //返回编码   200 默认成功
      private T data;
      protected T errorBody;
  
      public DataCommRes() {
      }
  
      public DataCommRes(Integer resultcode, String errormsg) {
          this.resultcode = resultcode;
          this.errormsg = errormsg;
      }
  
      public DataCommRes(ErrorCodeEnum errorCodeEnum, String errMsg, T data, Integer fox) {
          this.resultcode = Integer.parseInt(errorCodeEnum.getCode());
          this.msg = errorCodeEnum.getMsg();
          this.data = data;
          this.errormsg = errMsg;
      }
  
      public DataCommRes(ErrorCodeEnum errorCodeEnum, String errMsg) {
          this.resultcode = Integer.parseInt(errorCodeEnum.getCode());
          this.msg = errorCodeEnum.getMsg();
          this.errormsg = errMsg;
      }
  
      public static <T> DataCommRes<T> success(T data) {
          DataCommRes<T> res = new DataCommRes<>();
          res.setData(data);
          return res;
      }
  
      public static DataCommRes errorRes(String errormsg) {
          DataCommRes res = new DataCommRes<>();
          res.result = false;
          res.resultcode = null;
          res.errormsg = errormsg;
          return res;
      }
  
      public static <T> DataCommRes<T> errorBody(T errorBody) {
          DataCommRes res = new DataCommRes<>();
          res.result = false;
          res.resultcode = null;
          res.errorBody = errorBody;
          return res;
      }
  }
  
  ```

-  遇到多个构造器参数时要考虑使用构建器 

  > ​	静态工厂和构造器有个共同的局限性：他们都不能很好的扩展到大量的可选参数。常见的习惯都是采用重叠构造器模式。如下例子，构造器DataCommRes的重叠方式，相比之下，要比上一个例子简洁很多。
  >
  > ​	不过 重叠构造器模式对于多参数构造器来说还是可行，但是当有许多参数的时候，客户端代码会很难缩写，并且仍然较难以阅读和维护。

  ```java
  @Data
  public class DataCommRes<T> implements Serializable {
  
      protected String errormsg;      //一般是错误信息
      protected Boolean result = true;        //返回结果是否正常
      protected String msg;       //返回信息
      protected Integer resultcode = 200;     //返回编码   200 默认成功
      private T data;
      protected T errorBody;
  
      public DataCommRes() {
      }
  
      public DataCommRes(Integer resultcode, String errormsg) {
          this(true, resultcode, errormsg);
      }
      
      public DataCommRes(Boolean result, String resultcode, String errormsg) {
          this(result, StringUtils.isBlank(resultcode) ? null : Integer.valueOf(resultcode), errormsg);
      }
  
      public DataCommRes(Boolean result, Integer resultcode, String errormsg) {
          this.result = result;
          this.resultcode = resultcode;
          this.errormsg = errormsg;
      }
      
      public DataCommRes(ErrorCodeEnum errorCodeEnum, String errMsg) {
          this(errorCodeEnum, errMsg, null);
      }
      
      public DataCommRes(ErrorCodeEnum errorCodeEnum, String errMsg, T data) {
          this.resultcode = Integer.parseInt(errorCodeEnum.getCode());
          this.msg = errorCodeEnum.getMsg();
          this.data = data;
          this.errormsg = errMsg;
      }
  
      public static <T> DataCommRes<T> success(T data) {
          DataCommRes<T> res = new DataCommRes<>();
          res.setData(data);
          return res;
      }
  
      public static DataCommRes errorRes(String errormsg) {
          DataCommRes res = new DataCommRes<>();
          res.result = false;
          res.resultcode = null;
          res.errormsg = errormsg;
          return res;
      }
  
      public static <T> DataCommRes<T> errorBody(T errorBody) {
          DataCommRes res = new DataCommRes<>();
          res.result = false;
          res.resultcode = null;
          res.errorBody = errorBody;
          return res;
      }
  }
  ```

  > ​	参数比较多的情况下，你在开发的时候弄反两个参数的位置，也不会报错，这样的错误也很隐性，很难发现，出现问题有可能也很严重。
  >
  > ​	遇到很多可选构造参数的时候，还有第二种可选的代替方案--JavaBean模式，也就是创建好我们的类后，显示的调用 setter方法。
  >
  > ​	JavaBean的缺点：因为构造过程中分到了几个调用，`在构造过程中JavaBean可能处于不一致的状态`，有setter的方法存在，那么把类做成不可变那么这是一个不可能的事情。
  >
  > ​	对于这种情况，那么有第三种替代方案，就是建造器模式，lombok插件使用@Builder 注解就可以使用建造者模式了。和构造者相比，builder的优点就是比较灵活，可以在创建对象的时候就进行调整。**缺点：** 就是每次创建的时候，都必须先创建他的构建器、
  >
  > <font color="red">使用builder的时候，有时候会把原本写的默认值给清楚掉</font>

  ```java
  IssueResult result = IssueResult.builder()
                          .failNum(fail)
                          .successNum(success)
                          .issueCardErrorViews(issueCardErrorViews)
                          .build();
  ```

-  用私有构造器或者枚举类型强化`Singleton`属性 

  >  Singleton -> 指单列类，仅仅只是被实例化一次。在阿里巴巴的java指导中，单例模式的构造器都是建议私有，不对外公开来强化单例属性。
  >
  > ​	但是通过反射机制就可以调用私有构造器，为了防止这个情况，可以在构造器中，判断二次创建的时候直接抛出一个异常。

**2020-10-27**

-  通过私有构造器强化不可实例化的能力。

  > 工具类或者某些只包含静态方法或者静态域的类，不希望被实例化，java是默认为每个类，加载一个无参构造函数，为了防止这类类被实例化，我们将他们的构造函数加上私有属性。

-  优先考虑依赖注人来引用资源 

-  避免创建不必要的对象。

  ```java
  String s = new String("sss"); // 错误示范
  String s = "sss";
  ```

  > 第一条语句每次执行的时候都会创建一个新的String实例，这些动作完全没得必要，因为“sss”本身就是一个String的实例。<font color ="red">如果这条语句被放在一个循环里，会造出成千上万的对象。</font>
  >
  > 第二条语句，只包含了一个“sss"的字符串常量，这个对象会被重复使用。
  >
  >  
  >
  > 对于一个类同时提供`静态工厂方法` 和 `构造器` 的不可变类，优先使用静态工厂方法，避免创建不必要的对象。比如 new Integer() 和 Integer.valueOf()，优先使用 valueOf()。

  ​	有些对象创建的成本就非常的大，如果重复创建这类`昂贵的对象`，建议将他缓存下来使用，比如 java 的`pattern.matcher(s).matches()` 这个正则匹配，他每次创建Pattern后，就使用了一次，就被垃圾回收了。然而Pattern的创建成本很高，为了提升性能，让它成为这类初始化的一部分。

  ```java
  final Pattern pattern = Pattern.compile("[0-9]*");
  if (pattern.matcher(s).matches()) {
     // ...
  }
  ```

  ​	另一种创建多余对象的方法，称为`自动装箱`，它允许基本类型和装箱类型混用，按需自行装拆箱。我们在试用装箱类型的时候，阿里巴巴的规范往往会提示我们，该类型应该被替换成基本类型。![1603765389669](../../../markdownImage/1603765389669.png)

  ```java
  Long sum = 0L;
  for (long i = 0 ; i< Integer.MAX_VALUE; i++){
  	sum += i;
  }
  System.out.println(sum);
  ```

  ​		因为这段例子，试用了装箱类型，执行的速度会非常慢，因为sum声明成Long，执行的时候会被创建非常多的实例。将sum类型改成long，执行时间会大大增加。

  <font color="red"> 结论： 要优先使用基本类型而不是装箱基本类型，要当心无意识的自动装箱 </font>

-  消除过期的对象引用 

  > 如果不能消除过期对象的引用，那么这些引用将会慢慢堆积，从而造成内存泄漏。

  很多情况下，造成内存泄漏的原因都是没有消除过期对象，往往导致这些的问题都比较隐性，很难以及时发现。

  1. 自己管理内存的类，往往可能存在内存泄漏的问题。如下是真实的一个栈的例子。这个例子本身是没有任何问题，无论怎么样，都表现为逻辑正常。但是在pop函数中存在一个隐性的内存泄漏的问题，因为他将把size的值减少了，但是size减少之前的位置的对象依然存在，只是不在使用。应该改成

     ```java
     	public class Statck {
             private Object[] elements;
             private int size = 0;
             private static final int DEFAULT_INITIAL_CAPACITY = 16;
     
             public Statck() {
                 this.elements = new Object[DEFAULT_INITIAL_CAPACITY];
             }
     
             public void push(Object e) {
                 ensureCapacity();
                 elements[size++] = e;
             }
     
             public Object pop() {
                 if (size == 0) {
                     // 抛异常
                 }
                 return elements[--size];
             }
     
             private void ensureCapacity() {
                 if (elements.length == size) {
                     elements = Arrays.copyOf(elements, 2 * size + 1);
                 }
             }
     
         }
     ```

     ```java
     	public Object pop() {
                 if (size == 0) {
                     // 抛异常
                 }
                 Object element = elements[--size];
                 elements[size] = null;
                 return element;
         }
     ```

     2. 内存泄漏的第二个来源是缓存。一旦把对象放入缓存中，你慢慢的忘记这个对象的存在，那么内存泄漏就会产生了。

- 在处理必须关闭的资源的时候，try -with-resources 优先 try -finally

  > 在通常的try-finally来确保资源被关闭，正常来说是没有什么问题的，但是在try和finally模块都有可能出现异常，有些问题导致try中出现异常，在close也会出现异常，那么后面的那个异常会将前面的异常给覆盖掉，在问题回溯的时候是非常难找出根本原因的。
  >
  > try-with-resources:就把这些问题一下子全部解决了。完全不用担心try-finally存在的问题。

  ```java
  	static String fistlineOfFile(String path) throws Exception{
          try(BufferedReader br = new BufferedReader(new FileReader((path)))){
              return br.readLine();
          }
      }
      static void copy(String src, String dst)  throws Exception{
          try(InputStream in = new FileInputStream(src);
              OutputStream out = new FileOutputStream(dst)){
              byte[] buf = new byte[1024];
              int n;
              while ((n = in.read(buf)) >= 0){
                  out.write(buf, 0, n);
              }
          }
      }
  ```

// 第三章.



























