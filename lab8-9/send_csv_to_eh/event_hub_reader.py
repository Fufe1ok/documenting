import asyncio
import json
import logging

from azure.eventhub.aio import EventHubConsumerClient

logging.basicConfig(level=logging.INFO)

with open('./config.json', 'r') as f:
    config = json.load(f)

try:
    event_hub_args = config["event_hub"]
except KeyError as error:
    logging.error("Config error")
    raise


async def on_event(partition_context, event):
    print("Received the event: \"{}\" from the partition with ID: \"{}\"".format(event.body_as_str(encoding='UTF-8'),
                                                                                 partition_context.partition_id))
    await partition_context.update_checkpoint(event)


async def receive():
    client = EventHubConsumerClient.from_connection_string(**event_hub_args, consumer_group="$Default")
    async with client:
        await client.receive(
            on_event=on_event,
            starting_position="-1",  # "-1" is from the beginning of the partition.
        )
        # receive events from specified partition:
        # await client.receive(on_event=on_event, partition_id='0')

if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    loop.run_until_complete(receive())
