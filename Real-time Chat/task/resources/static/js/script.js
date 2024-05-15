const messageContainer = document.getElementById('messages');
const chatContainer = document.getElementById('chat-container');
const userList = document.getElementById('users');

let username = null;

// Get the username from the user input field
const loginContainer = document.getElementById('login-container');
const usernameInput = document.getElementById('input-username');
const loginForm = document.getElementById('login-form');

loginForm.addEventListener('submit', (event) => {
        event.preventDefault();
        username = usernameInput.value.trim();
        if (username) {
            loginContainer.remove();
            chatContainer.classList.remove("hidden");

            connect(username);
        }
    }
);


function appendMessageContainer(message) {
    messageContainer.append(createMessageContainerElement(message));
    messageContainer.lastElementChild.scrollIntoView({behavior: 'smooth'});
}

function createSenderElement(sender) {
    const senderElement = document.createElement('div');
    senderElement.classList.add('sender');
    senderElement.innerText = sender;
    return senderElement;
}

function createDateElement(date) {
    const dateElement = document.createElement('div');
    dateElement.classList.add('date');
    dateElement.innerText = date;
    return dateElement;
}

function createMessageElement(message) {
    const messageElement = document.createElement('div');
    messageElement.classList.add('message');
    messageElement.innerText = message;
    return messageElement;
}


function createMessageContainerElement(message) {
    const messageContainerElement = document.createElement('div');
    messageContainerElement.classList.add('message-container');
    messageContainerElement.append(createSenderElement(message.sender.name));
    messageContainerElement.append(createDateElement(message.date));
    messageContainerElement.append(createMessageElement(message.content));
    return messageContainerElement;
}

function createUserContainerElement(user){
    const userContainerElement = document.createElement('div');
    userContainerElement.classList.add('user-container');
    userContainerElement.append(createUserElement(user));

    const messageCounter = createMessageCounterElement(user);
    userContainerElement.append(messageCounter);

    userContainerElement.addEventListener('click', () => {
        clearChat();
        fetchPrivateChat(user).then((privateMessages) => {
            createPrivateChat(user, privateMessages);
        });
    });

    return userContainerElement;
}

function createUserElement(user) {
    const userElement = document.createElement('li');
    userElement.classList.add('user', 'group');
    userElement.innerText = user.name;
    userElement.setAttribute("key", user.userChatId);

    return userElement;
}

function createMessageCounterElement(user) {
    const messageCounter = document.createElement('span');
    messageCounter.classList.add('new-message-counter', "group");
    messageCounter.innerText = '0';

    if(newMessageCounter.get(user.userChatId) > 0){
        messageCounter.innerText = newMessageCounter.get(user.userChatId);
        messageCounter.classList.remove('hidden');
    } else {
        messageCounter.classList.add('hidden');
    }

    return messageCounter;
}

function createPrivateChat(user, messages){
    const otherChatUser = document.getElementById('chat-with');
    otherChatUser.innerText = `${user.name}`;

    const returnBtn = document.getElementById('public-chat-btn');
    returnBtn.classList.remove('hidden');

    returnBtn.addEventListener('click', recreatePublicChat);

    if(!publicChat){
        messages.forEach(message => {
            appendMessageContainer(message);
        });
    }
}

async function fetchPrivateChat(user) {
    publicChat = false;

    chatOpenWith = user;

    resetNewMessageCounter(user);

    return await fetchPrivateMessages();
}

function resetNewMessageCounter(user) {
    newMessageCounter.set(user.userChatId, 0);

    const userElement = document.querySelector(`[key="${user.userChatId}"]`);
    const messageCounter = userElement.nextElementSibling;

    // Check if the nextElementSibling has the new-message-counter class
    if (messageCounter && messageCounter.classList.contains('new-message-counter')) {
        messageCounter.innerText = '0';
        messageCounter.classList.add('hidden');

        return messageCounter;
    } else {
        console.error('The next element is not a new-message-counter');
        return null;
    }

}

function refreshSpecificUserInUserList(user) {
    const userElement = document.querySelector(`[key="${user.userChatId}"]`);
    const messageCounter = userElement.nextElementSibling;

    const userContainer = userElement.parentNode;

    userList.insertBefore(userContainer, userList.firstChild);


    if (messageCounter && messageCounter.classList.contains('new-message-counter')) {
        messageCounter.innerText = newMessageCounter.get(user.userChatId);
        messageCounter.classList.remove('hidden');

        return messageCounter;
    } else {
        console.error('The next element is not a new-message-counter');
        return null;
    }
}

function clearChat() {
    messageContainer.innerHTML = '';
}

async function recreatePublicChat() {
    publicChat = true;
    chatOpenWith = null;

    clearChat();
    const otherChatUser = document.getElementById('chat-with');
    otherChatUser.innerText = 'Public chat';

    const returnBtn = document.getElementById('public-chat-btn');
    returnBtn.classList.add('hidden');

    await fetchMessages();
}

function appendUserElement(user) {
    const usersContainer = document.getElementById('users');
    usersContainer.append(createUserContainerElement(user));
}
