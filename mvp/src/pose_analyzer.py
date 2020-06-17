import requests
import json

class PoseAnalyzer:
  def __init__(self):
    self.config = None

  def __del__(self):
    pass

  def analyze(self):

    with open('settings/conf.json') as f:
      self.config = json.load(f)

    APP_KEY = self.config['APP_KEY']
    IMAGE_URL = 'https://example.example.example'
    IMAGE_FILE_PATH = 'assets/example.jpg'
    session = requests.Session()
    session.headers.update({'Authorization': 'KakaoAK ' + APP_KEY})

    # # URL로 이미지 입력시
    # response = session.post('https://cv-api.kakaobrain.com/pose', data={'image_url': IMAGE_URL})
    # print(response.status_code, response.json())

    # 파일로 이미지 입력시
    with open(IMAGE_FILE_PATH, 'rb') as f:
      response = session.post('https://cv-api.kakaobrain.com/pose', files=[('file', f)])
      print(response.status_code, response.json())
