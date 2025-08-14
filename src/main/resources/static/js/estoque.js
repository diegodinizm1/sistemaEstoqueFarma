document.addEventListener('DOMContentLoaded', function() {
    // --- Variáveis Globais ---
    let estoqueCompleto = [];
    const corpoTabelaEstoque = document.getElementById('corpo-tabela-estoque');
    const buscarNomeInput = document.getElementById('buscarNome');
    const filtroStatusSelect = document.getElementById('filtroStatus');
    const ordenarPorSelect = document.getElementById('ordenarPor');
    const hoje = new Date();

    // --- Configuração dos Modais ---
    const entradaModalElement = document.getElementById('entradaModal');
    const saidaModalElement = document.getElementById('saidaModal');
    const correcaoModalElement = document.getElementById('correcaoModal');
    const correcaoModal = new bootstrap.Modal(correcaoModalElement);

    const correcaoEstoqueIdInput = document.getElementById('correcao-estoque-id');
    const correcaoMedicamentoInput = document.getElementById('correcao-medicamento');
    const correcaoLoteInput = document.getElementById('correcao-lote');
    const correcaoQuantidadeAtualInput = document.getElementById('correcao-quantidade-atual');
    const correcaoQuantidadeNovaInput = document.getElementById('correcao-quantidade-nova');
    const correcaoObservacaoTextarea = document.getElementById('correcao-observacao');
    const btnSalvarCorrecao = document.getElementById('btn-salvar-correcao');

    // --- Funções de Filtragem e Renderização da Tabela ---

    const renderizarTabela = (itens) => {
        corpoTabelaEstoque.innerHTML = '';
        if (itens.length === 0) {
            corpoTabelaEstoque.innerHTML = `<tr><td colspan="5" class="text-center text-muted p-4">Nenhum item em estoque encontrado.</td></tr>`;
            return;
        }

        itens.forEach(item => {
            console.log(item);
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${item.nomeMedicamento}</td>
                <td>${item.numeroLote}</td>
                <td>${formatarData(item.dataValidade)}</td>
                <td class="text-end fw-bold">${item.quantidadeDisponivel} ${item.unidadeMedida}</td>
                <td class="text-end">
                    <button class="btn btn-sm btn-warning btn-corrigir"
                            data-bs-toggle="modal" data-bs-target="#correcaoModal"
                            data-id="${item.id}"
                            data-medicamento="${item.nomeMedicamento}"
                            data-lote="${item.numeroLote}"
                            data-quantidade="${item.quantidadeDisponivel}">
                        <i class="bi bi-pencil-fill me-1"></i> Corrigir
                    </button>
                </td>
            `;
            corpoTabelaEstoque.appendChild(row);
        });
    };

    const aplicarFiltroEOrdenacao = () => {
        const termoBusca = buscarNomeInput.value.toLowerCase();
        const filtroStatus = filtroStatusSelect.value;
        const ordenacao = ordenarPorSelect.value;
        const hoje = new Date();

        let itensFiltrados = estoqueCompleto.filter(item => {
            const nomeCorresponde = item.nomeMedicamento.toLowerCase().includes(termoBusca);
            const validadeItem = new Date(item.dataValidade);
            const diferencaMeses = (validadeItem.getFullYear() - hoje.getFullYear()) * 12 + (validadeItem.getMonth() - hoje.getMonth());

            const statusCorresponde =
                filtroStatus === 'todos' ||
                (filtroStatus === 'vencidos' && validadeItem < hoje) ||
                (filtroStatus === 'proximos' && (diferencaMeses > 0 && diferencaMeses <= 3)) ||
                (filtroStatus === 'baixoEstoque' && item.quantidadeDisponivel <= 10 && item.quantidadeDisponivel > 0) ||
                (filtroStatus === 'emFalta' && item.quantidadeDisponivel === 0);

            return nomeCorresponde && statusCorresponde;
        });

        itensFiltrados.sort((a, b) => {
            switch (ordenacao) {
                case 'nome_asc':
                    return a.nomeMedicamento.localeCompare(b.nomeMedicamento);
                case 'nome_desc':
                    return b.nomeMedicamento.localeCompare(a.nomeMedicamento);
                case 'validade_asc':
                    return new Date(a.dataValidade) - new Date(b.dataValidade);
                case 'validade_desc':
                    return new Date(b.dataValidade) - new Date(a.dataValidade);
                case 'quantidade_asc':
                    return a.quantidadeDisponivel - b.quantidadeDisponivel;
                case 'quantidade_desc':
                    return b.quantidadeDisponivel - a.quantidadeDisponivel;
                default:
                    return 0;
            }
        });

        renderizarTabela(itensFiltrados);
    };

    const carregarEstoque = async () => {
        try {
            const response = await fetch('/estoque');
            if (response.redirected) window.location.href = response.url;
            if (!response.ok) throw new Error('Falha ao carregar o estoque.');
            estoqueCompleto = await response.json();
            aplicarFiltroEOrdenacao();
        } catch (error) {
            console.error('Erro ao carregar o estoque:', error);
            corpoTabelaEstoque.innerHTML = `<tr><td colspan="5" class="text-center text-danger p-4">Erro ao carregar o estoque.</td></tr>`;
        }
    };

    const formatarData = (dataString) => {
        const data = new Date(dataString);
        return data.toLocaleDateString('pt-BR');
    };

    // --- Lógica do Modal de Correção ---
    if (correcaoModalElement) {
        correcaoModalElement.addEventListener('show.bs.modal', (event) => {
            const button = event.relatedTarget;
            correcaoEstoqueIdInput.value = button.dataset.id;
            correcaoMedicamentoInput.value = button.dataset.medicamento;
            correcaoLoteInput.value = button.dataset.lote;
            correcaoQuantidadeAtualInput.value = button.dataset.quantidade;
            correcaoQuantidadeNovaInput.value = button.dataset.quantidade;
            correcaoObservacaoTextarea.value = '';
        });

        btnSalvarCorrecao.addEventListener('click', async () => {
            const idEstoque = correcaoEstoqueIdInput.value;
            const novaQuantidade = correcaoQuantidadeNovaInput.value;
            const observacao = correcaoObservacaoTextarea.value;
            const quantidadeAtual = correcaoQuantidadeAtualInput.value;

            if (novaQuantidade === '' || observacao === '') {
                alert('Por favor, preencha todos os campos obrigatórios.');
                return;
            }

            const confirmacao = confirm(`Confirma a correção da quantidade de ${quantidadeAtual} para ${novaQuantidade}?`);
            if (!confirmacao) {
                return;
            }

            try {
                const dados = { idEstoque, novaQuantidade: parseInt(novaQuantidade), observacao };
                const response = await fetch('/estoque/ajustar-contagem', {
                    method: 'PATCH',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(dados)
                });

                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(errorText || 'Falha ao salvar a correção.');
                }

                alert('Contagem corrigida com sucesso!');
                correcaoModal.hide();
                carregarEstoque();
            } catch (error) {
                console.error('Erro ao salvar correção:', error);
                alert(`Erro: ${error.message}`);
            }
        });
    }

    // --- Lógica dos Modais de Entrada e Saída (sem alterações) ---
    const entradaModal = new bootstrap.Modal(entradaModalElement);
    const entradaForm = document.getElementById('form-nova-entrada');
    const selectMedicamentoEntrada = document.getElementById('medicamento');
    const btnSalvarEntrada = document.getElementById('btn-salvar-entrada');

    entradaModalElement.addEventListener('show.bs.modal', () => {
        populaSelect(selectMedicamentoEntrada, '/medicamentos/ativos', 'Selecione um medicamento');
    });

    btnSalvarEntrada.addEventListener('click', () => entradaForm.requestSubmit());
    entradaForm.addEventListener('submit', (event) => handleFormSubmit(event, entradaForm, '/operacoes/entradas', entradaModal, 'Entrada registrada com sucesso!'));

    const saidaModal = new bootstrap.Modal(saidaModalElement);
    const saidaForm = document.getElementById('form-nova-saida');
    const selectMedicamentoSaida = document.getElementById('saida-medicamento');
    const selectSetorSaida = document.getElementById('saida-setor');
    const btnSalvarSaida = document.getElementById('btn-salvar-saida');

    saidaModalElement.addEventListener('show.bs.modal', () => {
        populaSelect(selectMedicamentoSaida, '/medicamentos/ativos', 'Selecione um medicamento');
        populaSelect(selectSetorSaida, '/setores', 'Selecione um setor');
    });

    btnSalvarSaida.addEventListener('click', () => saidaForm.requestSubmit());
    saidaForm.addEventListener('submit', (event) => handleFormSubmit(event, saidaForm, '/operacoes/saidas', saidaModal, 'Saída registrada com sucesso!'));

    // --- Funções Auxiliares (mantidas) ---
    const populaSelect = async (selectElement, endpoint, placeholder) => {
        selectElement.innerHTML = `<option value="" disabled selected>${placeholder}</option>`;
        try {
            const response = await fetch(endpoint);
            if (response.redirected) window.location.href = response.url;
            if (!response.ok) throw new Error('Falha ao carregar dados.');
            const data = await response.json();
            data.forEach(item => {
                const option = document.createElement('option');
                option.value = item.id;
                option.textContent = item.nome;
                selectElement.appendChild(option);
            });
        } catch (error) {
            console.error(`Erro ao popular select do endpoint ${endpoint}:`, error);
            selectElement.innerHTML = '<option value="" disabled selected>Erro ao carregar</option>';
        }
    };

    const handleFormSubmit = async (event, formElement, endpoint, modalInstance, successMessage) => {
        event.preventDefault();
        const formData = new FormData(formElement);
        const dados = Object.fromEntries(formData.entries());

        dados.idUsuario = 1;

        try {
            const response = await fetch(endpoint, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(dados)
            });
            if (response.redirected) window.location.href = response.url;
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Falha na operação.');
            }
            alert(successMessage);
            modalInstance.hide();
            carregarEstoque();
        } catch (error) {
            console.error(`Erro ao enviar para ${endpoint}:`, error);
            alert(`Erro: ${error.message}`);
        }
    };

    // --- Event Listeners para Busca e Filtros ---
    buscarNomeInput.addEventListener('input', aplicarFiltroEOrdenacao);
    filtroStatusSelect.addEventListener('change', aplicarFiltroEOrdenacao);
    ordenarPorSelect.addEventListener('change', aplicarFiltroEOrdenacao);

    // --- Inicialização ---
    carregarEstoque();
});