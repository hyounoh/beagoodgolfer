from src import *

from flask_restful import Resource, reqparse
from flask_restful_swagger_2 import swagger


class ApiResourceName(Resource):
  @swagger.doc({
    'description': 'api resource example',
    'tags'       : ['tags example'],
    'parameters' : [
      {
        "name"       : "sentence",
        "type"       : "string",
        'in'         : 'query',
        'required'   : True,
        "description": "input text"
      }
    ],
    'responses'  : {
      '200': {
        'description': 'success',
        'examples'   : {
          'application/json': {
            "message": 'success',
            "results": [
              {
                "sentence": 'input sentece'
              }
            ]
          }
        }
      },
      '500': {
        'description': 'error message',
        'examples'   : {
          'application/json': {
            "message": 'error message'
          }
        }
      }
    }
  })
  def get(self):

    # check args
    parser = reqparse.RequestParser()
    parser.add_argument('sentence', type=str)

    # handle requested args
    args = parser.parse_args()

    # handle exception
    sentence = args.get('sentence')
    if sentence is None:
      return output_json({
        "message": 'no sentence'
      }, 500)

    # do something...
    #
    #

    # response
    return output_json({
      "message": 'success get',
      "results": [
        {
          "sentence": sentence
        }
      ]
    }, 200)

  @swagger.doc({
    'description': 'api resource example',
    'tags'       : ['tags example'],
    'parameters' : [
      {
        "name"       : "sentence",
        "type"       : "string",
        'in'         : 'query',
        'required'   : True,
        "description": "input text"
      }
    ],
    'responses'  : {
      '200': {
        'description': 'success',
        'examples'   : {
          'application/json': {
            "message": 'success',
            "results": [
              {
                "sentence": 'input sentece'
              }
            ]
          }
        }
      },
      '500': {
        'description': 'error message',
        'examples'   : {
          'application/json': {
            "message": 'error message'
          }
        }
      }
    }
  })
  def post(self):
    # check args
    parser = reqparse.RequestParser()
    parser.add_argument('sentence', type=str)

    # handle requested args
    args = parser.parse_args()

    # handle exception
    sentence = args.get('sentence')
    if sentence is None:
      return output_json({
        "message": 'no sentence'
      }, 500)

    # do something...
    #
    #

    # response
    return output_json({
      "message": 'success post',
      "results": [
        {
          "sentence": sentence
        }
      ]
    }, 200)
