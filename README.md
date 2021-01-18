# elasticsearch index

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

