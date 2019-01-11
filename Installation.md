Installation steps for Learning Platform Infrastructure
-

Sunbird Learning Platform installation is automated with ansible as configuration management tool.

```sh
devops/
  playbooks.yml
  inventory/
    sample_env/
      group_vars/
        lp.yml
      hosts
  roles/
    ansible-roles
```

Above depicts the folder structure for ansible code distribution.

All the variables which can have default values, for example **cassandra_ip_address** is dynamically derived or constant values assigned and added in to defaults.
You can override that in group_vars though.

### Prerequisites

- [ansible v2.5.0](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html#latest-releases-via-pip)

**Instances Required**

1. learning and redis - 1 (2 cpu, 8GB Memory)
2. Search - 1 ( 2 cpu, 4GB Memory)
3. Neo4j - 1 (2 cpu, 8GB Memory)
4. cassandra - 1 ( 2 cpu, 4GB Memory)
5. elasticsearch(CompositeSearch) - 1 ( 2 cpu, 4GB Memory)
6. kafka and zookeeper - 1 (2 cpu, 8GB Memory)
7. yarn-master - 1 (2 cpu, 8GB Memory)
8. yarn-slave - 3 (2 cpu, 8GB Memory)


### Components in LP

1. Database
    - Cassandra
    - Neo4j
    - ElasticSearch
    - Redis
2. Event processors
    - Kafka
    - Yarn
3. Services
    - Learning
    - Search


### Variable Description

Mandatory variables to update

| Variable | Definition |
| --- | --- |
| env | Denotes the environement, for example dev, staging |
| azure_storage_secret | Azure storage secret for blob |
| azure_public_container | Azure container name to store contents |
| azure_account_name | Azure storage account name for storage |
| azure_account_key | Azure storage key |

> All values start with `vault_` in group_vars are passwords/keys you'll have to update

### Installing LP

> Please execute following playbooks in order.

General ansible execution format is
`ansible-playbook -i inventory/sample_env/hosts <playbook.yml>`

1.  lp_cassandra_provision.yml
2.  es_composite_search_cluster_setup.yml
3.  lp_learning_neo4j_provision.yml
4.  lp_learning_provision.yml
5.  lp_redis_provision.yml
6.  lp_search_provision.yml
7.  lp_cassandra_db_update.yml
8.  lp_learning_neo4j_deploy.yml
9.  lp_start_neo4j.yml
10.  lp_zookeeper_provision.yml
11.  lp_kafka_provision.yml
12.  lp_kafka_setup.yml
13.  lp_learning_deploy.yml
14.  lp_logstash_deploy.yml --extra-vars "remote=learningall"
15.  lp_search_deploy.yml
16.  lp_synctool_deploy.yml
17.  lp_logstash_deploy.yml --extra-vars "remote=searchall"
18.  lp_yarn_provision.yml
19.  lp_samza_deploy.yml
20.  lp_samza_telemetry_schemas.yml