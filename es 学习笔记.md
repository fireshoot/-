#### 基操

```shell
快速检查集群的健康状况
GET /_cat/health?v

快速查看集群中有哪些索引
GET /_cat/indices?v

索引操作
创建索引：PUT /test_index?pretty
删除索引：DELETE /test_index?pretty

CRUD操作
PUT /index/type/id
{
  "json数据"
}
PUT /ecommerce/product/1
{
    "name" : "gaolujie yagao",
    "desc" :  "gaoxiao meibai",
    "price" :  30,
    "producer" :      "gaolujie producer",
    "tags": [ "meibai", "fangzhu" ]
}
PUT /ecommerce/product/2
{
    "name" : "jiajieshi yagao",
    "desc" :  "youxiao fangzhu",
    "price" :  25,
    "producer" :      "jiajieshi producer",
    "tags": [ "fangzhu" ]
}
PUT /ecommerce/product/3
{
    "name" : "zhonghua yagao",
    "desc" :  "caoben zhiwu",
    "price" :  40,
    "producer" :      "zhonghua producer",
    "tags": [ "qingxin" ]
}

查询
GET /index/type/id
GET /ecommerce/product/1

修改--替换
PUT /ecommerce/product/1
{
    "name" : "jiaqiangban gaolujie yagao",
    "desc" :  "gaoxiao meibai",
    "price" :  30,
    "producer" :      "gaolujie producer",
    "tags": [ "meibai", "fangzhu" ]
}
PUT /ecommerce/product/1
{
    "name" : "jiaqiangban gaolujie yagao"
}

修改--更新
POST /ecommerce/product/1/_update
{
  "doc": {
    "name": "jiaqiangban gaolujie yagao"
  }
}

删除文档
DELETE /ecommerce/product/1

{
  "found": true,
  "_index": "ecommerce",
  "_type": "product",
  "_id": "1",
  "_version": 9,
  "result": "deleted",
  "_shards": {
    "total": 2,
    "successful": 1,
    "failed": 0
  }
}
```



#### 几种搜索方式

##### query string search

搜索全部商品：GET /ecommerce/product/**_search**

```shell
{
  "took": 2,
  "timed_out": false,
  "_shards": {
    "total": 5,
    "successful": 5,
    "failed": 0
  },
  "hits": {
    "total": 3,
    "max_score": 1,
    "hits": [
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "2",
        "_score": 1,
        "_source": {
          "name": "jiajieshi yagao",
          "desc": "youxiao fangzhu",
          "price": 25,
          "producer": "jiajieshi producer",
          "tags": [
            "fangzhu"
          ]
        }
      },
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "1",
        "_score": 1,
        "_source": {
          "name": "gaolujie yagao",
          "desc": "gaoxiao meibai",
          "price": 30,
          "producer": "gaolujie producer",
          "tags": [
            "meibai",
            "fangzhu"
          ]
        }
      },
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "3",
        "_score": 1,
        "_source": {
          "name": "zhonghua yagao",
          "desc": "caoben zhiwu",
          "price": 40,
          "producer": "zhonghua producer",
          "tags": [
            "qingxin"
          ]
        }
      }
    ]
  }
}
```

##### query DSL

DSL：Domain Specified Language，特定领域的语言

```shell
查询所有的商品
GET /ecommerce/product/_search
{
  "query": { "match_all": {} }
}

查询名称包含yagao的商品，同时按照价格降序排序
GET /ecommerce/product/_search
{
    "query" : {
        "match" : {
            "name" : "yagao"
        }
    },
    "sort": [
        { "price": "desc" }
    ]
}

分页查询商品，总共3条商品，假设每页就显示1条商品，现在显示第2页，所以就查出来第2个商品

GET /ecommerce/product/_search
{
  "query": { "match_all": {} },
  "from": 1,
  "size": 1
}

指定要查询出来商品的名称和价格就可以

GET /ecommerce/product/_search
{
  "query": { "match_all": {} },
  "_source": ["name", "price"]
}

```



#####  query filter

```shell
搜索商品名称包含yagao，而且售价大于25元的商品

GET /ecommerce/product/_search
{
    "query" : {
        "bool" : {
            "must" : {
                "match" : {
                    "name" : "yagao" 
                }
            },
            "filter" : {
                "range" : {
                    "price" : { "gt" : 25 } 
                }
            }
        }
    }
}

```



#####  full-text search（全文检索）传说中的分词查询

```shell
GET /ecommerce/product/_search
{
    "query" : {
        "match" : {
            "producer" : "yagao producer"
        }
    }
}

roducer这个字段，会先被拆解，建立倒排索引
special		4
yagao		4
producer	1,2,3,4
gaolujie	1
zhognhua	3
jiajieshi	2
yagao producer ---> yagao和producer

{
  "took": 4,
  "timed_out": false,
  "_shards": {
    "total": 5,
    "successful": 5,
    "failed": 0
  },
  "hits": {
    "total": 4,
    "max_score": 0.70293105,
    "hits": [
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "4",
        "_score": 0.70293105,
        "_source": {
          "name": "special yagao",
          "desc": "special meibai",
          "price": 50,
          "producer": "special yagao producer",
          "tags": [
            "meibai"
          ]
        }
      },
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "1",
        "_score": 0.25811607,
        "_source": {
          "name": "gaolujie yagao",
          "desc": "gaoxiao meibai",
          "price": 30,
          "producer": "gaolujie producer",
          "tags": [
            "meibai",
            "fangzhu"
          ]
        }
      },
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "3",
        "_score": 0.25811607,
        "_source": {
          "name": "zhonghua yagao",
          "desc": "caoben zhiwu",
          "price": 40,
          "producer": "zhonghua producer",
          "tags": [
            "qingxin"
          ]
        }
      },
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "2",
        "_score": 0.1805489,
        "_source": {
          "name": "jiajieshi yagao",
          "desc": "youxiao fangzhu",
          "price": 25,
          "producer": "jiajieshi producer",
          "tags": [
            "fangzhu"
          ]
        }
      }
    ]
  }
}
```



#####  phrase search（短语搜索）

跟全文检索相对应，相反，全文检索会将输入的搜索串拆解开来，去倒排索引里面去一一匹配，只要能匹配上任意一个拆解后的单词，就可以作为结果返回
phrase search，要求输入的搜索串，必须在指定的字段文本中，完全包含一模一样的，才可以算匹配，才能作为结果返回

```shell
GET /ecommerce/product/_search
{
    "query" : {
        "match_phrase" : {
            "producer" : "yagao producer"
        }
    }
}

{
  "took": 11,
  "timed_out": false,
  "_shards": {
    "total": 5,
    "successful": 5,
    "failed": 0
  },
  "hits": {
    "total": 1,
    "max_score": 0.70293105,
    "hits": [
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "4",
        "_score": 0.70293105,
        "_source": {
          "name": "special yagao",
          "desc": "special meibai",
          "price": 50,
          "producer": "special yagao producer",
          "tags": [
            "meibai"
          ]
        }
      }
    ]
  }
}
```



##### highlight search（高亮搜索结果）

```shell
GET /ecommerce/product/_search
{
    "query" : {
        "match" : {
            "producer" : "producer"
        }
    },
    "highlight": {
        "fields" : {
            "producer" : {}
        }
    }
}
```



#### 聚合搜搜

```shell
第一个分析需求：计算每个tag下的商品数量

GET /ecommerce/product/_search
{
#aggs : 聚合
  "aggs": {
  #group_by_tags 名称(自己定义)
    "group_by_tags": {
      "terms": { "field": "tags" }
    }
  }
}

将文本field的fielddata属性设置为true

PUT /ecommerce/_mapping/product
{
  "properties": {
    "tags": {
      "type": "text",
      "fielddata": true
    }
  }
}

GET /ecommerce/product/_search
{
  "size": 0,
  "aggs": {
    "all_tags": {
      "terms": { "field": "tags" }
    }
  }
}

{
  "took": 20,
  "timed_out": false,
  "_shards": {
    "total": 5,
    "successful": 5,
    "failed": 0
  },
  "hits": {
    "total": 4,
    "max_score": 0,
    "hits": []
  },
  "aggregations": {
    "group_by_tags": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 0,
      "buckets": [
        {
          "key": "fangzhu",
          "doc_count": 2
        },
        {
          "key": "meibai",
          "doc_count": 2
        },
        {
          "key": "qingxin",
          "doc_count": 1
        }
      ]
    }
  }
}
```



```shell
第二个聚合分析的需求：对名称中包含yagao的商品，计算每个tag下的商品数量

GET /ecommerce/product/_search
{
  "size": 0,
  "query": {
    "match": {
      "name": "yagao"
    }
  },
  "aggs": {
    "all_tags": {
      "terms": {
        "field": "tags"
      }
    }
  }
}

```



```shell
第三个聚合分析的需求：先分组，再算每组的平均值，计算每个tag下的商品的平均价格

GET /ecommerce/product/_search
{
    "size": 0,
    "aggs" : {
        "group_by_tags" : {
            "terms" : { "field" : "tags" },
            "aggs" : {
                "avg_price" : {
                    "avg" : { "field" : "price" }
                }
            }
        }
    }
}

{
  "took": 8,
  "timed_out": false,
  "_shards": {
    "total": 5,
    "successful": 5,
    "failed": 0
  },
  "hits": {
    "total": 4,
    "max_score": 0,
    "hits": []
  },
  "aggregations": {
    "group_by_tags": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 0,
      "buckets": [
        {
          "key": "fangzhu",
          "doc_count": 2,
          "avg_price": {
            "value": 27.5
          }
        },
        {
          "key": "meibai",
          "doc_count": 2,
          "avg_price": {
            "value": 40
          }
        },
        {
          "key": "qingxin",
          "doc_count": 1,
          "avg_price": {
            "value": 40
          }
        }
      ]
    }
  }
}

----------------------------------------------------------------------------------------------------------------

第四个数据分析需求：计算每个tag下的商品的平均价格，并且按照平均价格降序排序

GET /ecommerce/product/_search
{
    "size": 0,
    "aggs" : {
        "all_tags" : {
            "terms" : { "field" : "tags", "order": { "avg_price": "desc" } },
            "aggs" : {
                "avg_price" : {
                    "avg" : { "field" : "price" }
                }
            }
        }
    }
}

我们现在全部都是用es的restful api在学习和讲解es的所欲知识点和功能点，但是没有使用一些编程语言去讲解（比如java），原因有以下：

1、es最重要的api，让我们进行各种尝试、学习甚至在某些环境下进行使用的api，就是restful api。如果你学习不用es restful api，比如我上来就用java api来讲es，也是可以的，但是你根本就漏掉了es知识的一大块，你都不知道它最重要的restful api是怎么用的
2、讲知识点，用es restful api，更加方便，快捷，不用每次都写大量的java代码，能加快讲课的效率和速度，更加易于同学们关注es本身的知识和功能的学习
3、我们通常会讲完es知识点后，开始详细讲解java api，如何用java api执行各种操作
4、我们每个篇章都会搭配一个项目实战，项目实战是完全基于java去开发的真实项目和系统

----------------------------------------------------------------------------------------------------------------

第五个数据分析需求：按照指定的价格范围区间进行分组，然后在每组内再按照tag进行分组，最后再计算每组的平均价格

GET /ecommerce/product/_search
{
  "size": 0,
  "aggs": {
    "group_by_price": {
      "range": {
        "field": "price",
        "ranges": [
          {
            "from": 0,
            "to": 20
          },
          {
            "from": 20,
            "to": 40
          },
          {
            "from": 40,
            "to": 50
          }
        ]
      },
      "aggs": {
        "group_by_tags": {
          "terms": {
            "field": "tags"
          },
          "aggs": {
            "average_price": {
              "avg": {
                "field": "price"
              }
            }
          }
        }
      }
    }
  }
}
```



#### JAVA的基操

##### full-text search--<font color = "red">QueryBuilders是重点</font>

```java
public String fullTextQuerySingleField(String textQuery, boolean isPhraseQuery,
                String fieldName, int start, int count) {
    QueryBuilder qb;
    if (!isPhraseQuery)
        qb = QueryBuilders.matchQuery(fieldName, textQuery);
    else
        qb = QueryBuilders.matchPhraseQuery(fieldName, textQuery);
    SearchSourceBuilder ssb = new SearchSourceBuilder();
    ssb.query(qb);
    ssb.from(start);
    if (count > -1)
        ssb.size(count);
    
    return ssb.toString();
}
```

##### phrase search（短语搜索）

```java
MatchPhraseQueryBuilder matchPhraseQueryBuilder
                = QueryBuilders.matchPhraseQuery(fieldName, text);
 SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
 searchSourceBuilder.query(getMatchPhraseQueryBuilder(fieldName, text));
searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
 // 拿响应值
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

```





#### BoolQuery( ) 用于组合多个叶子或复合查询子句的默认查询

- must 相当于 与 & =
- must not 相当于 非 ~   ！=
- should 相当于 或  |   or 
- filter  过滤

```java
/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * boolean query and 条件组合查询
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
protected static QueryBuilder booleanQuery() {

    return QueryBuilders
            .boolQuery()
            .must(QueryBuilders.termQuery("name", "葫芦3033娃")) //条件都必须满足
            .must(QueryBuilders.termQuery("home", "山西省太原市7967街道"))
            .mustNot(QueryBuilders.termQuery("isRealMen", false))
            .should(QueryBuilders.termQuery("now_home", "山西省太原市"));//至少满足一个就可以 相当于in
}
```

#### 范围查询

```java
//闭区间查询
QueryBuilder qb1 = QueryBuilders.rangeQuery("${fieldName}").from(${fieldValue1}).to(${fieldValue2}); 
 
//开区间查询
QueryBuilder qb1 = QueryBuilders.rangeQuery("${fieldName}").from(${fieldValue1}, false).to(${fieldValue2}, false);
 
//大于
QueryBuilder qb1 = QueryBuilders.rangeQuery("${fieldName}").gt(${fieldValue});
 
 //大于等于
QueryBuilder qb1 = QueryBuilders.rangeQuery("${fieldName}").gte(${fieldValue}); 
 
//小于
QueryBuilder qb1 = QueryBuilders.rangeQuery("${fieldName}").lt(${fieldValue}); 
 
//小于等于
QueryBuilder qb1 = QueryBuilders.rangeQuery("${fieldName}").lte(${fieldValue});

```

#### 多条件查询

```java
ueryBuilder qb1 = QueryBuilders.moreLikeThisQuery(new String[]{"${fieldName1}"}, new String[]{"${fieldValue1}"}, null);
QueryBuilder qb2 = QueryBuilders.rangeQuery("${fieldName2}").gt("${fieldValue2}");
QueryBuilder qb3 = QueryBuilders.boolQuery().must(qb1).must(qb2);

```

