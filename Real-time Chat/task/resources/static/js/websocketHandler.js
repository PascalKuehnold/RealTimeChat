"use strict";

const messageForm = document.getElementById('message-form');
const messageInput = document.getElementById('input-msg');


messageForm.addEventListener('submit', sendMessage, true)

let newMessageCounter = new Map();

let stompClient = null;
let currentUser = {
    name: null,
    userChatId: null
};
let chatOpenWith = {
    name: null,
    userChatId: null
}
let publicChat = true;

function connect(username) {
    let socket = new SockJS('http://localhost:28852/chat');
    stompClient = Stomp.over(socket);

    currentUser.name = username;

    stompClient.connect({}, onConnected, onError);
}


async function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/chatroom/public', onPublicMessageReceived);
    stompClient.subscribe(`/user/${currentUser.name}/private`, onPrivateMessageReceived);

    let chatMessage = {
        date: new Date(),
        sender: currentUser.name,
        content: "",
        type: "JOIN"
    };
    stompClient.send("/app/chat.addUser", {}, JSON.stringify(chatMessage))

    await fetchMessages();
    await fetchOnlineUsers();


}

function onError(error) {
    //connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    //connectingElement.style.color = 'red';
    console.log(error);
}

async function fetchOnlineUsers() {
    const userList = document.getElementById('users');
    userList.innerHTML = '';

    await fetch('http://localhost:28852/online-users')
        .then(response => response.json())
        .then(users => {
            users.forEach(user => {
                if (user.name === currentUser.name) {
                    return;
                }
                appendUserElement(user);
            });
        });
}


async function fetchMessages() {
    await fetch('http://localhost:28852/messages')
        .then(response => response.json())
        .then(messages => {
            messages.forEach(message => {
                appendMessageContainer(message);
            });
        });
}

function getFormattedDate() {
    let date = new Date();
    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes();
}

function sendMessage(event) {
    event.preventDefault();

    let messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        let chatMessage = {
            date: getFormattedDate(),
            sender: currentUser,
            content: messageInput.value,
            type: publicChat ? "CHAT" : "PRIVATE"
        };

        if (!publicChat) {
            chatMessage.receiver = chatOpenWith;
        }

        // Send the message
        let destination = publicChat ? "/app/chat.sendMessage" : "/app/chat.sendPrivateMessage";
        stompClient.send(destination, {}, JSON.stringify(chatMessage));

        // Append the message to the sender's chat
        if(!publicChat) {
            appendMessageContainer(chatMessage);
            const userElement = document.querySelector(`[key="${chatOpenWith.userChatId}"]`);
            const userContainer = userElement.parentNode;

            userList.insertBefore(userContainer, userList.firstChild);
        }

        // Clear the message input field
        messageInput.value = '';

    }
}

async function onPrivateMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    console.log(message);

    if (chatOpenWith != null && message.type === "PRIVATE" && message.sender.userChatId === chatOpenWith.userChatId) {
        appendMessageContainer(message);

    } else if(message.type === "PRIVATE") {
        newMessageCounter.has(message.sender.userChatId) ? newMessageCounter.set(message.sender.userChatId, newMessageCounter.get(message.sender.userChatId) + 1) : newMessageCounter.set(message.sender.userChatId, 1);

        // rerender the specific user element in the userlist
        refreshSpecificUserInUserList(message.sender);

    }
}

async function fetchPrivateMessages() {
    const response = await fetch(`http://localhost:28852/private-messages`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            sender: currentUser,
            receiver: chatOpenWith
        })
    })

    return await response.json();
}

async function onPublicMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    console.log(message)

    if (message.type === 'JOIN' && currentUser.userChatId === null) {
        currentUser = {
            name: message.sender.name,
            userChatId: message.sender.userChatId
        }
    } else if (message.type === "JOIN") {
        await fetchOnlineUsers()
    } else if (message.type === "LEAVE") {
        await fetchOnlineUsers()
    } else if (message.type === "CHAT" && publicChat) {
        appendMessageContainer(message);
    }
}


