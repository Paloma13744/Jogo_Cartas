class Jogador(val nome: String) {
    val mao = mutableListOf<Carta>()
    var pontosDeVida = 10000 // Pontos iniciais
    val tabuleiro = mutableListOf<CartaMonstro>()
    private var tituloExibido = false

    fun exibirPontosDeVida() {
        println("$nome, seus pontos de vida atuais são: $pontosDeVida")
    }

    fun receberCarta(carta: Carta) { // Função para receber cartas
        if (!tituloExibido) {
            println("\n\uD83D\uDC7B || Jogo de Cartas Monstro || \uD83D\uDC09")
            tituloExibido = true
        }
        mao.add(carta)
        val tipo = when (carta) { // Verifica o tipo de carta e mostra ao jogador o tipo de carta recebida
            is CartaEquipamento -> "Equipamento"
            is CartaMonstro -> "Monstro"
            else -> "Desconhecido"
        }
        println("$nome recebeu a carta: ${carta.nome} : ${carta.obterTipo()} (Ataque: ${carta.ataque}, Defesa: ${carta.defesa})") // Exibindo o tipo da carta
    }

    fun posicionarMonstro() {
        val monstros = mao.filterIsInstance<CartaMonstro>()
        if (monstros.isNotEmpty()) {
            println("Escolha um monstro da sua mão para posicionar:")
            monstros.forEachIndexed { index, carta ->
                // Exibindo o nome do monstro, ataque e defesa
                println("${index + 1}) ${carta.nome} : ${carta.tipo}  (Ataque: ${carta.ataque}, Defesa: ${carta.defesa})")
            }
            val escolha = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolha?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
                // Verificando se o tabuleiro tem espaço para mais monstros
                if (tabuleiro.size < 5) {
                    tabuleiro.add(monstroEscolhido)  // Posiciona o monstro no tabuleiro
                    mao.remove(monstroEscolhido)     // Remove o monstro da mão
                    println("$nome posicionou o monstro ${monstroEscolhido.nome} no tabuleiro.")
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


    fun realizarAtaque(jogadorAdversario: Jogador) {
        // Filtra apenas as cartas de monstros no tabuleiro
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()

        if (monstros.isNotEmpty()) {
            // Mostra os monstros que o jogador pode escolher para atacar
            println("Escolha um monstro para atacar:")
            monstros.forEachIndexed { index, carta -> println("${index + 1}) ${carta.nome} (Ataque: ${carta.ataque}, Defesa: ${carta.defesa})") }

            // Lê a escolha do jogador
            val escolha = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolha?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
                // O jogador escolheu um monstro para atacar, agora escolhemos um monstro adversário para atacar
                val monstrosAdversarios = jogadorAdversario.tabuleiro.filterIsInstance<CartaMonstro>()

                if (monstrosAdversarios.isNotEmpty()) {
                    // Exibe os monstros adversários para o jogador escolher
                    println("Escolha um monstro adversário para atacar:")
                    monstrosAdversarios.forEachIndexed { index, carta ->
                        println("${index + 1}) ${carta.nome} (Ataque: ${carta.ataque}, Defesa: ${carta.defesa})")
                    }

                    // Lê a escolha do monstro adversário
                    val escolhaMonstroAdversario = readLine()?.toIntOrNull()
                    val monstroAdversarioEscolhido =
                        monstrosAdversarios.getOrNull(escolhaMonstroAdversario?.minus(1) ?: -1)

                    if (monstroAdversarioEscolhido != null) {
                        // Calculando o dano: Ataque do monstro escolhido - Defesa do monstro adversário
                        val dano = (monstroEscolhido.ataque - monstroAdversarioEscolhido.defesa).coerceAtLeast(0)

                        if (dano > 0) {
                            // Aplica o dano ao adversário
                            println("$nome atacou ${jogadorAdversario.nome} com ${monstroEscolhido.nome}!")
                            jogadorAdversario.pontosDeVida -= dano
                            println("${jogadorAdversario.nome} perdeu $dano pontos de vida!")

                            // Exibe os pontos de vida do adversário
                            if (jogadorAdversario.pontosDeVida <= 0) {
                                jogadorAdversario.pontosDeVida = 0
                                println("${jogadorAdversario.nome} foi derrotado!")
                            }
                        } else {
                            // Se o dano for zero ou negativo, significa que o monstro adversário bloqueou o ataque
                            println("O ataque foi bloqueado pela defesa do monstro adversário!")
                        }
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


    fun equiparMonstro() {
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()
        val equipamentos = mao.filterIsInstance<CartaEquipamento>()
        if (monstros.isNotEmpty() && equipamentos.isNotEmpty()) {
            println("Escolha um monstro para equipar:")
            monstros.forEachIndexed { index, carta -> println("${index + 1}) ${carta.nome}") }
            val escolhaMonstro = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolhaMonstro?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
                println("Escolha um equipamento para equipar:")
                equipamentos.forEachIndexed { index, carta -> println("${index + 1}) ${carta.nome}") }
                val escolhaEquipamento = readLine()?.toIntOrNull()
                val equipamentoEscolhido = equipamentos.getOrNull(escolhaEquipamento?.minus(1) ?: -1)

                if (equipamentoEscolhido != null) {
                    // Equipando o monstro com o equipamento escolhido
                    println("$nome equipou ${monstroEscolhido.nome} com ${equipamentoEscolhido.nome}")
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

    fun alterarEstadoMonstro() {
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()
        if (monstros.isNotEmpty()) {
            println("Escolha um monstro para alterar o estado:")
            monstros.forEachIndexed { index, carta -> println("${index + 1}) ${carta.nome}") }
            val escolha = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolha?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
                monstroEscolhido.alternarEstado()
            } else {
                println("Monstro inválido para alterar estado.")
            }
        } else {
            println("Você não tem monstros para alterar o estado!")
        }
    }


    fun descartarCarta() {
        if (mao.isNotEmpty()) {
            println("Escolha uma carta para descartar:")
            mao.forEachIndexed { index, carta -> println("${index + 1}) ${carta.nome}") }
            val escolha = readLine()?.toIntOrNull()
            val cartaEscolhida = mao.getOrNull(escolha?.minus(1) ?: -1)
            if (cartaEscolhida != null) {
                descartarCarta()
            } else {
                println("Opção inválida.")
            }
        } else {
            println("Você não tem cartas na mão!")
        }
    }

    fun resetarAtaques() { // Muda o estado do monstro para defesa
        tabuleiro.forEach { it.estado = MonstroState.DEFESA }
    }

    fun escolherAcao(jogo: Jogo) {  // Menu interativo para os jogadores
        println("1)\uD83D\uDC09Posicionar um novo monstro no tabuleiro")
        println("2)⚙\uFE0FEquipar um monstro com uma carta de equipamento")
        println("3)\uD83D\uDDD1\uFE0FDescartar uma carta da mão")
        println("4)⚔\uFE0FRealizar um ataque contra o oponente")
        println("5)\uD83D\uDD04 Alterar o estado de um monstro (ataque/defesa)")
        println("6)\uD83D\uDC4B Sair")
        println("$nome, selecione uma opção:")

        val opcao = readLine()?.toIntOrNull()

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

            5 -> alterarEstadoMonstro()  // Fixed indentation issue
            6 -> {
                println("Saindo do jogo... O jogo terminou!")
                jogo.finalizarJogo()  // Finaliza o jogo
            }

            else -> println("Opção inválida!")
        }


    }
}