package com.mrpowergamerbr.loritta.commands.nashorn;

import com.mrpowergamerbr.loritta.commands.CommandContext;

/**
 * Contexto do comando Nashorn executado, é simplesmente um wrapper "seguro" para comandos em JavaScript, para que
 * a Loritta possa controlar os comandos executados de uma maneira segura (para não abusarem da API do Discord)
 */
public class NashornContext {
	public String mensagem;
	private CommandContext context; // Context original, jamais poderá ser usado pelo script!
	private int sentMessages = 0; // Quantas mensagens foram enviadas, usado para não levar rate limit
	private long lastMessageSent = 0L; // Quando foi a última mensagem enviada

	public NashornContext(CommandContext context) {
		this.context = context;
		this.mensagem = context.getMessage().getContent();
	}

	public void responder(String mensagem) {
		long diff = System.currentTimeMillis() - lastMessageSent;

		if (sentMessages >= 3) {
			if (diff > 2000) {
				throw new LorittaNashornException("Mais de 3 mensagens em menos de 2 segundos!");
			} else {
				diff = 0L;
				sentMessages = 0;
			}
		}

		sentMessages++;
		diff = System.currentTimeMillis();
		context.sendMessage(context.getAsMention(true) + mensagem);
	}

	public void enviarMensagem(String mensagem) {
		long diff = System.currentTimeMillis() - lastMessageSent;

		if (sentMessages >= 3) {
			if (diff > 2000) {
				throw new LorittaNashornException("Mais de 3 mensagens em menos de 2 segundos!");
			} else {
				diff = 0L;
				sentMessages = 0;
			}
		}

		sentMessages++;
		diff = System.currentTimeMillis();
		context.sendMessage(mensagem);
	}

	public String pegarArgumento(int idx) {
		return context.getArgs()[idx];
	}

	public boolean argumento(int idx, String mensagem) {
		return mensagem.equals(context.getArgs()[idx]);
	}
}
