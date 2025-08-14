document.addEventListener('DOMContentLoaded', function() {
    const tabelaCorpo = document.getElementById('corpo-tabela-movimentacoes');
    const paginacaoNav = document.getElementById('paginacao-movimentacoes');

    // Elementos de filtro
    const filtroTipo = document.getElementById('filtroTipo');
    const filtroMedicamento = document.getElementById('filtroMedicamento');
    const filtroSetor = document.getElementById('filtroSetor');
    const filtroDataInicio = document.getElementById('filtroDataInicio');
    const filtroDataFim = document.getElementById('filtroDataFim');
    const btnAplicarFiltros = document.getElementById('btn-aplicar-filtros');

    // Elementos do modal de relatório
    const relatorioModalElement = document.getElementById('relatorioModal');
    const relatorioModal = new bootstrap.Modal(relatorioModalElement);
    const dataRelatorioInput = document.getElementById('dataRelatorio');
    const btnGerarRelatorioModal = document.getElementById('btn-gerar-relatorio-modal');

    // Arrays globais para armazenar os dados dos selects
    let medicamentos = [];
    let setores = [];

    // Array global para armazenar todas as movimentações
    let movimentacoesCompletas = [];
    const itensPorPagina = 15;

    // Função para renderizar uma página da tabela (sem alterações)
    const renderizarTabela = (movimentacoes) => {
        tabelaCorpo.innerHTML = '';
        if (movimentacoes.length === 0) {
            tabelaCorpo.innerHTML = '<tr><td colspan="7" class="text-center p-4 text-muted">Nenhuma movimentação encontrada com os filtros aplicados.</td></tr>';
            return;
        }

        movimentacoes.forEach(mov => {
            const tr = document.createElement('tr');
            const dataFormatada = new Date(mov.dataHora).toLocaleString('pt-BR');
            const tipoClasse = mov.tipo === 'ENTRADA' ? 'text-success' : 'text-danger';
            const tipoTexto = mov.tipo.replace('_', ' ');

            tr.innerHTML = `
                <td>${dataFormatada}</td>
                <td>${mov.nomeProduto}</td>
                <td>${mov.numeroLote}</td>
                <td><span class="fw-bold ${tipoClasse}">${tipoTexto}</span></td>
                <td class="text-end">${mov.quantidade} ${mov.unidadeMedida}</td>
                <td>${mov.nomeUsuario}</td>
                <td>${mov.descricao || '-'}</td>
                <td>${mov.nomeSetorDestino || '-'}</td>
            `;
            tabelaCorpo.appendChild(tr);
        });
    };

    // Função para renderizar a paginação (sem alterações)
    const renderizarPaginacao = (totalItens, paginaAtual) => {
        paginacaoNav.innerHTML = '';
        const totalPaginas = Math.ceil(totalItens / itensPorPagina);
        if (totalPaginas <= 1) return;

        const ul = document.createElement('ul');
        ul.className = 'pagination justify-content-center mt-4';

        ul.innerHTML += `
            <li class="page-item ${paginaAtual === 0 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${paginaAtual - 1}">Anterior</a>
            </li>
        `;

        for (let i = 0; i < totalPaginas; i++) {
            ul.innerHTML += `
                <li class="page-item ${i === paginaAtual ? 'active' : ''}">
                    <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
                </li>
            `;
        }

        ul.innerHTML += `
            <li class="page-item ${paginaAtual === totalPaginas - 1 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${paginaAtual + 1}">Próximo</a>
            </li>
        `;

        paginacaoNav.appendChild(ul);
    };

    // Função para aplicar os filtros e renderizar a tabela (sem alterações)
    const aplicarFiltros = (page = 0) => {
        const termoTipo = filtroTipo.value;
        const idMedicamento = filtroMedicamento.value;
        const idSetor = filtroSetor.value;
        const dataInicio = filtroDataInicio.value ? new Date(filtroDataInicio.value + 'T00:00:00') : null;
        const dataFim = filtroDataFim.value ? new Date(filtroDataFim.value + 'T23:59:59') : null;

        const nomeMedicamentoSelecionado = idMedicamento ? medicamentos.find(m => m.id == idMedicamento)?.nome : '';
        const nomeSetorSelecionado = idSetor ? setores.find(s => s.id == idSetor)?.nome : '';

        const movimentacoesFiltradas = movimentacoesCompletas.filter(mov => {
            const dataMovimentacao = new Date(mov.dataHora);

            const filtroTipo = !termoTipo || mov.tipo === termoTipo;
            const filtroMedicamento = !idMedicamento || mov.nomeProduto === nomeMedicamentoSelecionado;
            const filtroSetor = !idSetor || mov.nomeSetorDestino === nomeSetorSelecionado;
            const filtroDataInicio = !dataInicio || dataMovimentacao >= dataInicio;
            const filtroDataFim = !dataFim || dataMovimentacao <= dataFim;

            return filtroTipo && filtroMedicamento && filtroSetor && filtroDataInicio && filtroDataFim;
        });

        const inicio = page * itensPorPagina;
        const fim = inicio + itensPorPagina;
        const itensNaPagina = movimentacoesFiltradas.slice(inicio, fim);

        renderizarTabela(itensNaPagina);
        renderizarPaginacao(movimentacoesFiltradas.length, page);
    };

    // Função para buscar todos os dados de uma vez (chamada inicial)
    const carregarMovimentacoesIniciais = async () => {
        try {
            const response = await fetch('/movimentacoes/all');

            if (response.redirected) {
                window.location.href = response.url;
                return;
            }
            if (!response.ok) {
                throw new Error('Falha ao carregar o histórico de movimentações.');
            }

            movimentacoesCompletas = await response.json();
            aplicarFiltros(0);

        } catch (error) {
            console.error('Erro ao carregar movimentações:', error);
            tabelaCorpo.innerHTML = `<tr><td colspan="7" class="text-center text-danger p-4">${error.message}</td></tr>`;
        }
    };

    // Função para popular os selects de filtro (sem alterações)
    const popularSelects = async () => {
        medicamentos = await populaSelect(filtroMedicamento, '/medicamentos', 'Todos');
        setores = await populaSelect(filtroSetor, '/setores', 'Todos');
    };

    // Função auxiliar para popular um <select> (sem alterações)
    const populaSelect = async (selectElement, endpoint, placeholder) => {
        selectElement.innerHTML = `<option value="">${placeholder}</option>`;
        try {
            const response = await fetch(endpoint);
            if (!response.ok) throw new Error('Falha ao carregar dados.');
            const data = await response.json();
            data.forEach(item => {
                const option = document.createElement('option');
                option.value = item.id;
                option.textContent = item.nome;
                selectElement.appendChild(option);
            });
            return data;
        } catch (error) {
            console.error(`Erro ao popular select do endpoint ${endpoint}:`, error);
            return [];
        }
    };

    // Event listener para a paginação (sem alterações)
    paginacaoNav.addEventListener('click', function(event) {
        event.preventDefault();
        const target = event.target;
        if (target.tagName === 'A' && target.hasAttribute('data-page')) {
            const pageNumber = parseInt(target.getAttribute('data-page'));
            if (!isNaN(pageNumber)) {
                aplicarFiltros(pageNumber);
            }
        }
    });

    // Event listener para o botão de aplicar filtros (sem alterações)
    btnAplicarFiltros.addEventListener('click', () => aplicarFiltros(0));

    // Lógica ajustada para o botão de relatório
    btnGerarRelatorioModal.addEventListener('click', async () => {
        const dataSelecionada = dataRelatorioInput.value;
        if (!dataSelecionada) {
            alert('Por favor, selecione uma data.');
            return;
        }

        try {
            const response = await fetch(`/relatorios/saida-diaria?data=${dataSelecionada}`);
            if (response.redirected) window.location.href = response.url;
            if (!response.ok) throw new Error('Falha ao buscar dados do relatório.');
            const dadosRelatorio = await response.json();
            relatorioModal.hide();
            gerarPaginaDeImpressao(dadosRelatorio, dataSelecionada);

        } catch (error) {
            console.error('Erro ao gerar relatório:', error);
            alert(error.message);
        }
    });

    const gerarPaginaDeImpressao = (relatorio, dataSelecionada) => {
        const dataFormatada = new Date(dataSelecionada + 'T00:00:00').toLocaleDateString('pt-BR');
        const setores = relatorio.nomeSetores;
        const dados = relatorio.dados;

        let tabelaHtml = '';

        if (dados.length === 0) {
            tabelaHtml = '<p class="text-center mt-4">Nenhuma saída registada nesta data.</p>';
        } else {
            const cabecalhoHtml = `
                <tr>
                    <th>Medicamento</th>
                    <th>Lote</th>
                    ${setores.map(setor => `<th class="text-center">${setor}</th>`).join('')}
                </tr>
            `;

            const linhasHtml = dados.map(item => {
                const celulasSetores = setores.map(setor => {
                    const quantidade = item.quantidadePorSetor[setor] || 0;
                    return `<td class="text-center">${quantidade}</td>`;
                }).join('');

                return `
                    <tr>
                        <td>${item.nomeMedicamento}</td>
                        <td>${item.numeroLote}</td>
                        ${celulasSetores}
                    </tr>
                `;
            }).join('');

            tabelaHtml = `
                <table class="table table-bordered table-sm">
                    <thead>${cabecalhoHtml}</thead>
                    <tbody>${linhasHtml}</tbody>
                </table>
            `;
        }

        const conteudoImpressao = `
            <html>
                <head>
                    <title>Relatório de Saídas - ${dataFormatada}</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
                    <style>
                        @media print { .no-print { display: none; } }
                        body { padding: 1.5rem; font-family: sans-serif; }
                        h1, h2 { text-align: center; margin-bottom: 1rem; }
                        table { font-size: 0.8rem; }
                    </style>
                </head>
                <body>
                    <h1>Relatório de Saídas Diárias por Setor</h1>
                    <h2>Data: ${dataFormatada}</h2>
                    ${tabelaHtml}
                    <div class="mt-5 text-center no-print">
                        <button class="btn btn-primary" onclick="window.print()">Imprimir</button>
                        <button class="btn btn-secondary" onclick="window.close()">Fechar</button>
                    </div>
                </body>
            </html>
        `;

        const printWindow = window.open('', '_blank');
        printWindow.document.write(conteudoImpressao);
        printWindow.document.close();
    };

    // Inicialização da página
    popularSelects();
    carregarMovimentacoesIniciais();
});