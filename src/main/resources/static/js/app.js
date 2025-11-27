const API = '/api/v1/lie/ask';
const form = document.getElementById('chat-form');
const input = document.getElementById('chat-input');
const msgs = document.getElementById('messages');

function addMessage(text, who='ai'){
    const div = document.createElement('div');
    div.className = `msg ${who}`;
    div.textContent = text;
    msgs.appendChild(div);
    msgs.scrollTop = msgs.scrollHeight;
}

form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const text = input.value.trim();
    if (!text) return;
    addMessage(text, 'me');
    input.value = '';

    const typing = document.createElement('div');
    typing.className = 'msg ai';
    typing.textContent = 'thinking…';
    msgs.appendChild(typing);
    msgs.scrollTop = msgs.scrollHeight;

    try {
        const res = await fetch(API, {
            method: 'POST',
            headers: {'Content-Type':'application/json'},
            body: JSON.stringify({ text })
        });
        if (!res.ok) throw new Error(await res.text() || 'Request failed');

        const data = await res.json();
        typing.remove();
        addMessage(data.answer ?? '(no answer)', 'ai');
    } catch (err) {
        typing.remove();
        addMessage('Error: ' + err.message, 'ai');
    }
});
