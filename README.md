# elasticsearch index

# nori 형태소 분석기 설치
bin/elasticsearch-plugin install analysis-nori

# create
PUT apt_search
{
  "settings": {
    "analysis": {
      "analyzer": {
        "nori_discard": {
          "tokenizer": "nori_t_discard",
          "filter": [
            "my_stop_f",
            "my_shingle"
          ]
        }
      },
      "tokenizer": {
        "nori_t_discard": {
          "type": "nori_tokenizer",
          "decompound_mode": "discard",
          "user_dictionary": "user_dic/apt_name_dic.txt"
        }
      },
       "filter": {
        "my_shingle": {
          "type": "shingle",
          "token_separator": "",
          "max_shingle_size": 4
        },
        "my_stop_f":{
          "type":"stop",
          "stopwords":[
            "동"
          ]
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "apt_name": {
        "type": "text",
        "fields": {
          "nori_discard": {
            "type": "text",
            "analyzer": "nori_discard"
          }
        }
      }
    }
  }
}

# check termvector (how is it saved)

GET apt_search/_termvectors/2?fields=apt_name.nori_discard


# check query (interoperation with Springboot server)


GET apt_search/_search
{
  "query": {
    "match": {
      "apt_name.nori_discard" : "뜨리에체"
    }
  }
}

# maria db status check
systemctl status mariadb

# maria db restart and check status
systemctl daemon-reload
systemctl start mariadb


# create user (maria db)
CREATE USER 'jin'@'%' IDENTIFIED BY '비밀번호';

# grant auth
GRANT ALL ON apt.*(db name: delete) to 'jin'@'%' (ip 대역폭) IDENTIFIED BY '비밀번호' WITH GRANT OPTION;
FLUSH PRIVILEGES;

