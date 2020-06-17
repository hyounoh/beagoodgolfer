import json
import requests
import werkzeug

APP_KEY = ""
with open('settings/conf.json') as f:
  APP_KEY = json.load(f)['APP_KEY']

print(APP_KEY)