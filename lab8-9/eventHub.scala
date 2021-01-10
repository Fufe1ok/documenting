import org.apache.spark.eventhubs.{ ConnectionStringBuilder, EventHubsConf, EventPosition }
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

// To connect to an Event Hub, EntityPath is required as part of the connection string.
// Here, we assume that the connection string from the Azure portal does not have the EntityPath part.
val appID = "a9f15680-6c3d-48b3-8081-4401aaa53730"
val password = "sB9F_e2WTcM.mP16isveqJ_5O2_O9BJK-v"
val tenantID = "0609f82c-8b22-4cf0-85b2-de552a0a0a55"
val fileSystemName = "lab9"
val storageAccountName = "laboratorna9"
val connectionString = ConnectionStringBuilder("Endpoint=sb://labour9.servicebus.windows.net;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=tuXTIxAlZs5CNbpuvdcPU33W2fVJjnsiI/wKrdJlEVc=")
  .setEventHubName("ehinstance")
  .build
val eventHubsConf = EventHubsConf(connectionString)
  .setStartingPosition(EventPosition.fromEndOfStream)

var streamingInputDF = 
  spark.readStream
    .format("eventhubs")
    .options(eventHubsConf.toMap)
    .load()

val filtered = streamingInputDF.select (
  from_unixtime(col("enqueuedTime").cast(LongType)).alias("enqueuedTime")
     , get_json_object(col("body").cast(StringType), "$.year").alias("year")
     , get_json_object(col("body").cast(StringType), "$.leading_cause").alias("leading_cause")
     , get_json_object(col("body").cast(StringType), "$.sex").alias("sex")
     , get_json_object(col("body").cast(StringType), "$.race_ethnicity").alias("race")
     , get_json_object(col("body").cast(StringType), "$.deaths").alias("deaths").cast(DoubleType)
     , get_json_object(col("body").cast(StringType), "$.death_rate").alias("death_rate").cast(DoubleType)
     , get_json_object(col("body").cast(StringType), "$.death_rate_adjusted").alias("death_rate_adjusted").cast(DoubleType)
)

filtered.writeStream
  .format("com.databricks.spark.csv")
  .outputMode("append")
  .option("checkpointLocation", "<path>")
  .start("<path>")