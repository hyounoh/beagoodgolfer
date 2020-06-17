from src import *
from src.routes import *

from flask_restful import Resource, reqparse
from flask_restful_swagger_2 import swagger


class PoseImage(Resource):
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
    parser.add_argument('image', type=werkzeug.datastructures.FileStorage, location='files')

    # handle requested args
    args = parser.parse_args()

    # handle exception
    image = args.get('image')
    if image is None:
      return output_json({
        "message": 'no image'
      }, 500)

    session = requests.Session()
    session.headers.update({'Authorization': 'KakaoAK ' + APP_KEY})
    response = session.post('https://cv-api.kakaobrain.com/pose', files=[('file', image)])

    # response
    if response.status_code == 200:
      return output_json({
        "message": 'success to analyze pose',
        "results": response.json()
      }, 200)
    else:
      return output_json({
        "message": "failed to analyze pose"
      }, 500)


class PoseImageTest(Resource):
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
    parser.add_argument('image', type=werkzeug.datastructures.FileStorage, location='files')

    # handle requested args
    args = parser.parse_args()

    # handle exception
    image = args.get('image')
    if image is None:
      return output_json({
        "message": 'no image'
      }, 500)

    # response
    return output_json({
      "message": 'success to analyze pose',
      "results": [
        {
            "area": 62435.52,
            "bbox": [
                99.2,
                99.9,
                159.6,
                391.2
            ],
            "category_id": 1,
            "keypoints": [
                236.5,
                201.5,
                0.8961,
                244.5,
                198.5,
                0.0142,
                237.5,
                193.5,
                0.8669,
                223.5,
                178.5,
                0.0044,
                222.5,
                178.5,
                0.8582,
                222.5,
                218.5,
                0.7456,
                166.5,
                178.5,
                0.6264,
                181.5,
                184.5,
                0.5273,
                112.5,
                175.5,
                0.7876,
                149.5,
                136.5,
                0.6272,
                137.5,
                132.5,
                0.7462,
                144.5,
                296.5,
                0.6407,
                112.5,
                283.5,
                0.6502,
                178.5,
                372.5,
                0.8119,
                128.5,
                363.5,
                0.7557,
                148.5,
                453.5,
                0.732,
                124.5,
                457.5,
                0.7172
            ],
            "score": 0.6464
        }
    ]
    }, 200)
