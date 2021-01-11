import json
import logging

from azure.eventhub import EventHubProducerClient
from flask import Flask
from flask_restful import Api

with open('./config.json', 'r') as f:
    config = json.load(f)

try:
    event_hub_args = config["event_hub"]
except KeyError as error:
    logging.error("Config error")
    raise

# Main flask app
app = Flask(__name__)

# flask-restful api
api = Api(app)

# Azure Event hub client
event_hub_client = EventHubProducerClient.from_connection_string(**event_hub_args)
