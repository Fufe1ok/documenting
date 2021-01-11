import logging

from controllers.EventHubController import EventHubController
from extensions import api, app

logging.basicConfig(level=logging.INFO)


@app.route('/')
def hello_world():
    return 'Lab 9'


api.add_resource(EventHubController, "/eventhub")


if __name__ == '__main__':

    app.run(debug=True)
