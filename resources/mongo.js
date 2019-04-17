db.users.insertOne({
    username: "yajneshvar",
    first_name: "yajneshvar",
    last_name: "arumugam",
    email: "yajneshvar.arumugam@gmail.com",
    roles: ["Admin", "Staff"],
    address: {street: "704-250 St George Street", city: "Toronto", postal_code: "M5R3L8", country: "Canada"}
})

db.time_entries.insertOne({
    task_des: "Teaching",
    total_hours: 8,
    user_id: "5cb67d98139a323dd366f4e7",
    date: '2019-04-18T16:14:25'
})