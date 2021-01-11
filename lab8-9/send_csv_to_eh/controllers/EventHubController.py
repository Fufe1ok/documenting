from flask_restful import reqparse, Resource

from workers.EventHubWriter import EventHubWriter
from workers.UrlFileReader import UrlFileReader


class EventHubController(Resource):

    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument("url")

        self.url_file_reader = UrlFileReader()
        self.event_hub_writer = EventHubWriter()

    def post(self):
        url = self.parser.parse_args().get('url')
        data = self.url_file_reader.read_file_from_url(url)

        self.event_hub_writer.write_to_event_hub(data)

        return "Done", 200
