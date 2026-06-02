const apiBase = '/tarefas';
const priorityBadge = document.getElementById('badge');
const prioritySelect = document.getElementById('prioridade');
const taskForm = document.getElementById('task-form');
const diasContainer = document.getElementById('dias-container');
const template = document.getElementById('task-card');

const dayOrder = [
  'SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO', 'DOMINGO'
];

const dayNames = {
  SEGUNDA: 'Segunda',
  TERCA: 'Terça',
  QUARTA: 'Quarta',
  QUINTA: 'Quinta',
  SEXTA: 'Sexta',
  SABADO: 'Sábado',
  DOMINGO: 'Domingo'
};

const priorityClasses = {
  URGENTE: 'vermelho',
  MEDIANO: 'amarelo',
  NO_PRAZO: 'verde'
};

function updateBadge() {
  const value = prioritySelect.value;
  priorityBadge.textContent = value;
  priorityBadge.className = 'badge badge-' + priorityClasses[value];
}

async function fetchTasks() {
  const res = await fetch(apiBase);
  const body = await res.json();
  return body.dados || [];
}

function groupByDay(tasks) {
  return tasks.reduce((groups, task) => {
    const day = task.diaSemana || 'SEM_DIA';
    if (!groups[day]) groups[day] = [];
    groups[day].push(task);
    return groups;
  }, {});
}

function createCard(task) {
  const clone = template.content.cloneNode(true);
  const card = clone.querySelector('.task-card');
  const title = clone.querySelector('.task-title');
  const description = clone.querySelector('.task-description');
  const badge = clone.querySelector('.task-badge');
  const meta = clone.querySelector('.task-meta');
  const checkbox = clone.querySelector('.task-toggle');
  const toggleLabel = clone.querySelector('.toggle-label');

  title.textContent = task.titulo;
  description.textContent = task.descricao;
  badge.textContent = task.prioridade;
  badge.className = `task-badge ${priorityClasses[task.prioridade] || 'verde'}`;
  meta.textContent = `Data: ${task.dataTarefa} • Criada: ${new Date(task.dataCriacao).toLocaleString()}`;
  checkbox.checked = task.concluida;
  toggleLabel.textContent = task.concluida ? 'Concluída' : 'Iniciada';
  if (task.concluida) card.classList.add('status-concluida');

  checkbox.addEventListener('change', async () => {
    const next = checkbox.checked ? 'concluir' : 'iniciar';
    await toggleTaskStatus(task.id, next);
    loadTasks();
  });

  return clone;
}

function renderTasks(tasks) {
  diasContainer.innerHTML = '';
  const groups = groupByDay(tasks);

  dayOrder.forEach((day) => {
    const tasksForDay = groups[day] || [];
    const section = document.createElement('section');
    section.className = 'day-box';
    section.innerHTML = `<h3>${dayNames[day]}</h3>`;

    if (tasksForDay.length === 0) {
      const empty = document.createElement('div');
      empty.className = 'empty-state';
      empty.textContent = 'Nenhuma tarefa para este dia.';
      section.appendChild(empty);
    } else {
      tasksForDay.forEach((task) => {
        section.appendChild(createCard(task));
      });
    }

    diasContainer.appendChild(section);
  });
}

async function addTask(data) {
  const res = await fetch(apiBase, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  });
  return res.json();
}

async function toggleTaskStatus(id, action) {
  await fetch(`${apiBase}/${id}/${action}`, { method: 'PATCH' });
}

async function loadTasks() {
  try {
    const tasks = await fetchTasks();
    renderTasks(tasks);
  } catch (error) {
    console.error('Erro ao carregar tarefas:', error);
  }
}

taskForm.addEventListener('submit', async (event) => {
  event.preventDefault();
  const formData = new FormData(taskForm);
  const payload = {
    titulo: formData.get('titulo'),
    descricao: formData.get('descricao'),
    prioridade: formData.get('prioridade'),
    diaSemana: formData.get('diaSemana'),
    dataTarefa: formData.get('dataTarefa')
  };

  await addTask(payload);
  taskForm.reset();
  prioritySelect.value = 'URGENTE';
  updateBadge();
  loadTasks();
});

prioritySelect.addEventListener('change', updateBadge);

updateBadge();
loadTasks();
