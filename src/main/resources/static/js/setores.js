document.addEventListener('DOMContentLoaded', function() {
    // --- Seletores de Elementos ---
    const tabelaCorpo = document.getElementById('corpo-tabela-setores');
    const modalElement = document.getElementById('setorModal');
    const setorModal = new bootstrap.Modal(modalElement);
    const formSetor = document.getElementById('form-setor');
    const btnSalvar = document.getElementById('btn-salvar-setor');
    const modalTitle = document.querySelector('#setorModal .modal-title');
    const nomeSetorInput = document.getElementById('nomeSetor');
    const modalAlertPlaceholder = document.querySelector('#setorModal #modal-alert-placeholder');

    let editandoId = null;

    // --- Funções ---

    const carregarSetores = async () => {
        try {
            const response = await fetch('/setores/alfabetico');
            if (response.redirected) window.location.href = response.url;
            if (!response.ok) throw new Error('Falha ao carregar setores.');

            const setores = await response.json();
            tabelaCorpo.innerHTML = '';

            if (setores.length === 0) {
                tabelaCorpo.innerHTML = '<tr><td colspan="3" class="text-center p-4 text-muted">Nenhum setor cadastrado.</td></tr>';
                return;
            }

            setores.forEach(setor => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${setor.id}</td>
                    <td>${setor.nome}</td>
                    <td class="text-end">
                        <button class="btn btn-sm btn-outline-primary me-2 btn-editar" data-id="${setor.id}" data-nome="${setor.nome}">Editar</button>
                        <button class="btn btn-sm btn-outline-danger btn-excluir" data-id="${setor.id}">Excluir</button>
                    </td>
                `;
                tabelaCorpo.appendChild(tr);
            });
        } catch (error) {
            console.error('Erro:', error);
            tabelaCorpo.innerHTML = `<tr><td colspan="3" class="text-center text-danger p-4">${error.message}</td></tr>`;
        }
    };

    const excluirSetor = async (id) => {
        try {
            const response = await fetch(`/setores/${id}`, { method: 'DELETE' });
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Falha ao excluir.');
            }
            carregarSetores();
        } catch (error) {
            console.error('Erro ao excluir:', error);
            alert(`Erro: ${error.message}`);
        }
    };

    // --- Event Listeners ---

    modalElement.addEventListener('show.bs.modal', () => {
        if (!editandoId) {
            modalTitle.textContent = 'Novo Setor';
            formSetor.reset();
            modalAlertPlaceholder.innerHTML = '';
        }
    });

    modalElement.addEventListener('hidden.bs.modal', () => {
        editandoId = null;
    });

    tabelaCorpo.addEventListener('click', (event) => {
        const target = event.target;
        if (target.classList.contains('btn-editar')) {
            editandoId = target.dataset.id;
            const nome = target.dataset.nome;

            modalTitle.textContent = `Editando Setor #${editandoId}`;
            nomeSetorInput.value = nome;
            modalAlertPlaceholder.innerHTML = '';
            setorModal.show();
        }

        if (target.classList.contains('btn-excluir')) {
            const id = target.dataset.id;
            if (confirm(`Tem a certeza que deseja excluir o setor ID #${id}?`)) {
                excluirSetor(id);
            }
        }
    });

    btnSalvar.addEventListener('click', () => formSetor.requestSubmit());
    formSetor.addEventListener('submit', async (event) => {
        event.preventDefault();
        modalAlertPlaceholder.innerHTML = '';
        const nome = nomeSetorInput.value;

        const url = editandoId ? `/setores/${editandoId}` : '/setores';
        const method = editandoId ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nome })
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Falha ao salvar.');
            }

            setorModal.hide();
            carregarSetores();
        } catch (error) {
            console.error('Erro ao salvar:', error);
            modalAlertPlaceholder.innerHTML = `<div class="alert alert-danger">${error.message}</div>`;
        }
    });

    // --- Inicialização ---
    carregarSetores();
});
