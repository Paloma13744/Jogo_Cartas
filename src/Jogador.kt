class Jogador(val nome: String) {
    val mao = mutableListOf<Carta>()
    var pontosDeVida = 10000 // Pontos iniciais
    val tabuleiro = mutableListOf<CartaMonstro>()
    private var tituloExibido = false


    fun receberCarta(carta: Carta) { // Função para receber cartas
        if (!tituloExibido) {
            println("\n\uD83D\uDC7B || Jogo de Cartas Monstro || \uD83D\uDC09")
            tituloExibido = true
        }
        mao.add(carta)
        val tipo = when (carta) { // Verifica o tipo de carta e mostra ao joagdor o tipo de carta recebida
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

    fun realizarAtaque() {
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()
        if (monstros.isNotEmpty()) {
            println("Escolha um monstro para atacar:")
            monstros.forEachIndexed { index, carta -> println("${index + 1}) ${carta.nome}") }
            val escolha = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolha?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
                // Lógica para atacar o oponente, por exemplo, atacar o monstro adversário
                println("$nome está atacando com ${monstroEscolhido.nome}!")
            } else {
                println("Monstro inválido para atacar.")
            }
        } else {
            println("Você não tem monstros para atacar!")
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

    fun escolherAcao(jogo: Jogo) {  // Menu interativo para os joagdores
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
            4 -> realizarAtaque()
            5 -> alterarEstadoMonstro()
            6 -> {
                println("Saindo do jogo... O jogo terminou!")
                jogo.finalizarJogo()  // Finaliza o jogo

            }

            else -> println("Opção inválida!")
        }
    }


}