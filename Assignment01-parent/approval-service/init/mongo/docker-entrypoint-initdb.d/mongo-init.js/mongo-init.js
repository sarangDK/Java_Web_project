print('START');

db = db.getSiblingDB('approval-service');

db.createUser(
    {
        user: 'admin',
        pwd: 'password',
        roles: [ {role: 'readWrite', db: 'event-service'}]
    }
);

db.createCollection('approval');

print('END')