configs = {"fs.azure.account.auth.type": "OAuth",
           "fs.azure.account.oauth.provider.type": "org.apache.hadoop.fs.azurebfs.oauth2.ClientCredsTokenProvider",
           "fs.azure.account.oauth2.client.id": "a9f15680-6c3d-48b3-8081-4401aaa53730",
           "fs.azure.account.oauth2.client.secret": "sB9F_e2WTcM.mP16isveqJ_5O2_O9BJK-v",
           "fs.azure.account.oauth2.client.endpoint": "https://login.microsoftonline.com/0609f82c-8b22-4cf0-85b2-de552a0a0a55/oauth2/token",
           "fs.azure.createRemoteFileSystemDuringInitialization": "true"}

# Optionally, you can add <directory-name> to the source URI of your mount point.
dbutils.fs.mount(
  source = "abfss://lab9@laboratorna9.dfs.core.windows.net/",
  mount_point = "/mnt/lab9",
  extra_configs = configs)

display(dbutils.fs.ls('/mnt/lab9'))