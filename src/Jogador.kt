class Jogador(val nome: String) {
    val mao = mutableListOf<Carta>()
    var pontosDeVida = 10000 // Pontos iniciais
    val tabuleiro = mutableListOf<CartaMonstro>()
    private var tituloExibido = false
    var equipamentoUsado: Boolean = false

    fun exibirPontosDeVida() { // Função para exibir os pontos de vida do jogador
        println("$nome, seus pontos de vida atuais são: $pontosDeVida")
    }


    fun receberCarta(carta: Carta) {  // Função para o jogador receber uma carta
        if (!tituloExibido) {
            println("\n\uD83D\uDC7B || Jogo de Cartas Monstro || \uD83D\uDC09")
            tituloExibido = true
        }
        mao.add(carta)// Adiciona a carta à mão do jogador
        val tipo = when (carta) { // Verifica o tipo de carta e mostra ao jogador o tipo de carta recebida
            is CartaEquipamento -> {
                println("$nome recebeu a carta: ${carta.nome} : ${carta.obterTipo()} (Ataque: ${carta.bonusAtaque}, Defesa: ${carta.bonusDefesa}")
                "Equipamento"
            }
            is CartaMonstro -> {
                println("$nome recebeu a carta: ${carta.nome} : ${carta.obterTipo()} (Ataque: ${carta.ataque}, Defesa: ${carta.defesa})")
                "Monstro"
            }
            else -> "Desconhecido"
        }


    }


    fun posicionarMonstro() { // Função para o jogador posicionar um monstro no tabuleiro
        val monstros = mao.filterIsInstance<CartaMonstro>()
        if (monstros.isNotEmpty()) {
            println("Escolha um monstro da sua mão para posicionar:")
            monstros.forEachIndexed { index, carta ->
                println("${index + 1}) ${carta.nome} : ${carta.tipo}  (Ataque: ${carta.ataque}, Defesa: ${carta.defesa})")
            }
            val escolha = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolha?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
                // Pergunta ao jogador em qual estado ele deseja colocar o monstro
                println("Escolha o estado do monstro ${monstroEscolhido.nome}:")
                println("1) Ataque")
                println("2) Defesa")
                val estadoEscolhido = readLine()?.toIntOrNull()

                when (estadoEscolhido) {
                    1 -> monstroEscolhido.estado = MonstroState.ATAQUE
                    2 -> monstroEscolhido.estado = MonstroState.DEFESA
                    else -> {
                        println("Opção inválida, o monstro será colocado em estado de defesa por padrão.")
                        monstroEscolhido.estado = MonstroState.DEFESA
                        println();
                    }
                }

                // Verifica se o tabuleiro tem espaço para mais monstros
                if (tabuleiro.size <= 5) {
                    tabuleiro.add(monstroEscolhido)  // Posiciona o monstro no tabuleiro
                    mao.remove(monstroEscolhido)     // Remove o monstro da mão
                    println("$nome posicionou o monstro ${monstroEscolhido.nome} no tabuleiro em estado ${monstroEscolhido.estado}.")
                } else {
                    println("O tabuleiro está cheio! Não é possível posicionar mais monstros.")
                }
            } else {
                println("Opção inválida ou monstro não encontrado.")
            }
        } else {
            println("Você não tem monstros na sua mão!")
        }
    }

    // Função para realizar um ataque
    fun realizarAtaque(jogadorAdversario: Jogador) {
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()

        if (monstros.isNotEmpty()) {
            // Mostra os monstros que o jogador pode escolher para atacar
            println("Escolha um monstro para atacar:")
            monstros.forEachIndexed { index, carta ->
                println("${index + 1}) ${carta.nome} (Ataque: ${carta.ataque}, Defesa: ${carta.defesa}, Já atacou: ${carta.jaAtacou}, Estado: ${carta.estado})")
            }

            val escolha = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolha?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
                if (monstroEscolhido.jaAtacou) {
                    println("Este monstro já atacou nesta rodada e não pode atacar novamente.")
                    return
                }

                // Escolhe um monstro adversário para atacar
                val monstrosAdversarios = jogadorAdversario.tabuleiro.filterIsInstance<CartaMonstro>()

                if (monstrosAdversarios.isNotEmpty()) {
                    println("Escolha um monstro adversário para atacar:")
                    monstrosAdversarios.forEachIndexed { index, carta ->
                        println("${index + 1}) ${carta.nome} (Ataque: ${carta.ataque}, Defesa: ${carta.defesa}, Estado: ${carta.estado})")
                    }

                    // Lê a escolha do monstro adversário
                    val escolhaMonstroAdversario = readLine()?.toIntOrNull()
                    val monstroAdversarioEscolhido =
                        monstrosAdversarios.getOrNull(escolhaMonstroAdversario?.minus(1) ?: -1)

                    if (monstroAdversarioEscolhido != null) {
                        // Verifica o estado do monstro adversário
                        if (monstroAdversarioEscolhido.estado == MonstroState.DEFESA) {
                            // Ataque contra monstro em DEFESA
                            if (monstroEscolhido.ataque > monstroAdversarioEscolhido.defesa) {
                                println("${monstroAdversarioEscolhido.nome} foi destruído!")
                                jogadorAdversario.tabuleiro.remove(monstroAdversarioEscolhido)

                                // O adversário perde 2000 pontos de vida
                                jogadorAdversario.pontosDeVida -= 2000  // Regra bônus(Paloma e Thiago)
                                println("${jogadorAdversario.nome} perdeu 2000 pontos de vida!")
                            } else {
                                val danoAoAtacante =
                                    (monstroAdversarioEscolhido.defesa - monstroEscolhido.ataque).coerceAtLeast(0)
                                pontosDeVida -= danoAoAtacante
                                println("O ataque falhou! ${monstroEscolhido.nome} sofreu $danoAoAtacante de dano!")
                            }
                        } else {
                            // Ataque contra monstro em ATAQUE
                            val dano = (monstroEscolhido.ataque - monstroAdversarioEscolhido.ataque).coerceAtLeast(0)
                            if (dano > 0) {
                                println("${monstroEscolhido.nome} atacou ${monstroAdversarioEscolhido.nome}!")
                                jogadorAdversario.pontosDeVida -= dano
                                println("${jogadorAdversario.nome} perdeu $dano pontos de vida!")
                            } else {
                                println("O ataque foi bloqueado pelo monstro adversário!")
                            }
                        }
                        // Marca o monstro como já tendo atacado
                        monstroEscolhido.jaAtacou = true

                        // Exibe a pontuação de vida atual de ambos os jogadores
                        mostrarPontuacaoAtual(jogadorAdversario)

                    } else {
                        println("Monstro adversário inválido para atacar.")
                    }
                } else {
                    println("O adversário não tem monstros para atacar!")
                }
            } else {
                println("Monstro inválido para atacar.")
            }
        } else {
            println("Você não tem monstros para atacar!")
        }
    }


    fun equiparMonstro() {     // Função para equipar um monstro com um equipamento
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()
        val equipamentos = mao.filterIsInstance<CartaEquipamento>()

        if (monstros.isNotEmpty() && equipamentos.isNotEmpty()) {
            println("Escolha um monstro para equipar:")
            monstros.forEachIndexed { index, carta -> println("${index + 1}) ${carta.nome} (Ataque: ${carta.ataque}, Defesa: ${carta.defesa})") }
            val escolhaMonstro = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolhaMonstro?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
                println("Escolha um equipamento para equipar:")
                equipamentos.forEachIndexed { index, carta ->
                    println("${index + 1}) ${carta.nome} (Ataque: +${carta.bonusAtaque}, Defesa: +${carta.bonusDefesa})")
                }
                val escolhaEquipamento = readLine()?.toIntOrNull()
                val equipamentoEscolhido = equipamentos.getOrNull(escolhaEquipamento?.minus(1) ?: -1)

                if (equipamentoEscolhido != null) {
                    // Aplica os bônus do equipamento ao monstro
                    monstroEscolhido.ataque += equipamentoEscolhido.bonusAtaque
                    monstroEscolhido.defesa += equipamentoEscolhido.bonusDefesa

                    // Remove o equipamento da mão do jogador
                    mao.remove(equipamentoEscolhido)

                    println("$nome equipou ${monstroEscolhido.nome} com ${equipamentoEscolhido.nome}!")
                    println("${monstroEscolhido.nome} agora tem Ataque: ${monstroEscolhido.ataque} e Defesa: ${monstroEscolhido.defesa}")
                } else {
                    println("Equipamento inválido.")
                }
            } else {
                println("Monstro inválido.")
            }
        } else {
            println("Você não tem monstros ou equipamentos disponíveis!")
        }
    }

    fun finalizarVez(){
        return
    }

    fun alterarEstadoMonstro() { // Função para alterar o estado de um monstro no tabuleiro
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()
        if (monstros.isNotEmpty()) {
            println("Escolha um monstro para alterar o estado:")
            monstros.forEachIndexed { index, carta ->
                println("${index + 1}) ${carta.nome} - Estado atual: ${carta.estado}")
            }
            val escolha = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolha?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
                // Exibe o estado atual do monstro antes de alterar
                println("Estado atual de ${monstroEscolhido.nome}: ${monstroEscolhido.estado}")

                // Alterna o estado do monstro
                monstroEscolhido.alternarEstado()

                // Exibe o novo estado após a alteração
                println("O estado de ${monstroEscolhido.nome} foi alterado para: ${monstroEscolhido.estado}")
            } else {
                println("Monstro inválido para alterar estado.")
            }
        } else {
            println("Você não tem monstros para alterar o estado!")
        }
    }

    // Função para descartar cartas quando o jogador tem mais de 10 cartas na mão
    fun descartarCarta() {
        if (mao.size > 10) {
            println("Você tem mais de 10 cartas na mão. Você precisa descartar algumas.")
            while (mao.size > 10) {
                println("Escolha uma carta para descartar:")
                mao.forEachIndexed { index, carta -> println("${index + 1} ${carta.nome}: ${carta.tipo}  (Ataque: ${carta.ataque}, Defesa: ${carta.defesa})") }
                val escolha = readLine()?.toIntOrNull()
                val cartaEscolhida = mao.getOrNull(escolha?.minus(1) ?: -1)

                if (cartaEscolhida != null) {
                    mao.remove(cartaEscolhida)
                    println("${cartaEscolhida.nome} : ${cartaEscolhida.tipo} foi descartada!")
                } else {
                    println("Opção inválida.")
                }
            }
        } else {
            println("Você já está dentro do limite de 10 cartas na mão.")
        }
    }

    fun resetarAtaques() { // Muda o estado do monstro para defesa
        tabuleiro.forEach { it.estado = MonstroState.DEFESA }
    }

    fun mostrarPontuacaoAtual(jogadorAdversario: Jogador) {  //  pontos de vida de cada jogador
        println("${this.nome}, sua pontuação de vida atual é: $pontosDeVida")
        println("${jogadorAdversario.nome}, a pontuação de vida atual é: ${jogadorAdversario.pontosDeVida}")
    }


    fun escolherAcao(jogo: Jogo) {  // Menu interativo para os jogadores
        var continuarJogando = true  // Flag para controlar o loop

        while (continuarJogando) {
            println("1)\uD83D\uDC09Posicionar um novo monstro no tabuleiro")
            println("2)⚙\uFE0FEquipar um monstro com uma carta de equipamento")
            println("3)\uD83D\uDDD1\uFE0FDescartar uma carta da mão")
            println("4)⚔\uFE0FRealizar um ataque contra o oponente")
            println("5)\uD83D\uDD04 Alterar o estado de um monstro (ataque/defesa)")
            println("6)\uD83D\uDC4B Finalizar vez")
            println("7)\uD83D\uDC4B Sair")
            println("$nome, selecione uma opção:")

            val opcao = readLine()?.toIntOrNull()

            if (opcao == null || opcao !in 1..7) {
                println("Opção inválida. Por favor, escolha uma opção de 1 a 7.")
                continue // Pula para a próxima iteração
            }

            when (opcao) {
                1 -> posicionarMonstro()
                2 -> equiparMonstro()
                3 -> descartarCarta()
                4 -> {
                    val oponente = jogo.obterOponente(this)
                    if (oponente != null) {
                        realizarAtaque(oponente)
                    } else {
                        println("Não foi possível encontrar um oponente!")
                    }
                }
                5 -> alterarEstadoMonstro()
                6 -> {
                    finalizarVez()
                    continuarJogando = false // Encerra o loop após finalizar a vez

                    println("Vez Finalizada")
                }
                7 -> {
                    println("Saindo do jogo... O jogo terminou!")
                    jogo.finalizarJogo()
                    continuarJogando = false // Encerra o loop se o jogador escolher sair
                }
                else -> println("Opção inválida!")
            }
        }
    }
}