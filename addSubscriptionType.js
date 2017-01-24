/*

Lancer le script pour réinitialiser la BDD concernant les type d'abonnements: 

Exemple:
C:\Program Files\MongoDB\Server\3.4\bin>mongo C:\Users\DamnAug\workspace\Linkus\addSubscriptionType.js

*/

// Connection to the database
var db = connect('127.0.0.1:27017/mydb');

// ** Drop subscriptionType **
db.subscriptionType.drop();

// ** Collection subscription **
print("Creating subscriptionType...");

db.subscriptionType.insert({
    _id:"1",
    type: "description",
    length: "1",
    unit: "year",
    price: "1.99"
});

db.subscriptionType.insert({
    _id:"2",
    type: "description",
    length: "6",
    unit: "month",
    price: "0.99"
});

db.subscriptionType.insert({
    _id:"10",
    type: "friend",
    length: "1",
    unit: "year",
    price: "1.99"
});

db.subscriptionType.insert({
    _id:"11",
    type: "friend",
    length: "6",
    unit: "month",
    price: "1.99"
});

print("Done.");

var allsubscriptionType = db.subscriptionType.find();

print("All subscription Types ("+ allsubscriptionType.count() +"): ");

while(allsubscriptionType.hasNext()) {
    var subscriptionType = allsubscriptionType.next();
    print(" subscription type: " + subscriptionType.type + ", duree: " + subscriptionType.length + " " + subscriptionType.unit);
}