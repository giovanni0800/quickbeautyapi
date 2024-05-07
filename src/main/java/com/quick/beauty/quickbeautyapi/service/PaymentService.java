package com.quick.beauty.quickbeautyapi.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.quick.beauty.quickbeautyapi.entities.Payment;
import com.quick.beauty.quickbeautyapi.repositories.ClientRepository;
import com.quick.beauty.quickbeautyapi.repositories.PaymentRepository;
import com.quick.beauty.quickbeautyapi.repositories.ProfessionalRepository;


@Service
public class PaymentService {
	
	@Autowired
	private PaymentRepository repoPay;
	
	@Autowired
	private ClientRepository repoCli;
	
	@Autowired
	private ProfessionalRepository repoPro;

	
	public ResponseEntity<?> realizePixPayment(Map<String, Object> requestData) {
		// URL do endpoint que deseja acessar
		String url = "https://api.mercadopago.com/v1/payments";

		Payment pay = new Payment();

		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString();

		// Cabeçalhos da requisição
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("accept", "application/json");
		headers.set("Authorization", "Bearer TEST-8331715131964198-081622-3e0473075157fcd324087c871f5b8ca8-1453671488");
		headers.set("X-Idempotency-Key", uuidString);

		// Corpo da requisição
		String requestBody = "{\n"
				+ "  \"transaction_amount\":200,\n"
				+ "  \"description\": \"Servcos esteticos\",\n"
				+ "  \"payment_method_id\": \"pix\",\n"
				+ "  \"payer\": {\n"
				+ "    \"email\": \"email@email.com\",\n"
				+ "    \"first_name\": \"Teste\",\n"
				+ "    \"last_name\": \"De Pagamento\",\n"
				+ "    \"identification\": {\n"
				+ "        \"type\": \"CPF\",\n"
				+ "        \"number\": \"19119119100\"\n"
				+ "    },\n"
				+ "    \"address\": {\n"
				+ "        \"zip_code\": \"02132190\",\n"
				+ "        \"street_name\": \"Rua Sem nome\",\n"
				+ "        \"street_number\": \"21\",\n"
				+ "        \"neighborhood\": \"Bairro\",\n"
				+ "        \"city\": \"Cidade\",\n"
				+ "        \"federal_unit\": \"SP\"\n"
				+ "    }\n"
				+ "  }\n"
				+ "}";

		// Criando um objeto RestTemplate
		RestTemplate restTemplate = new RestTemplate();

		// Criando uma entidade de requisição HTTP com os cabeçalhos e o corpo
		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

		// Enviando a requisição POST e recebendo a resposta
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		// Extraindo o código de status e o corpo da resposta
		HttpStatusCode statusCode = response.getStatusCode();
		String responseBody = response.getBody();
		
		if (requestData.containsKey("price") && requestData.containsKey("client")
				&& requestData.containsKey("professional")) {
			Double price = Double.valueOf( requestData.get("price").toString() );
			Long client = Long.valueOf(requestData.get("client").toString());
			Long professional = Long.valueOf(requestData.get("professional").toString());

			// Verifica se algum dos parâmetros é nulo
			if (price != null && client != null && professional != null) {

				if ((repoCli.findByUserId(client) != null) 
						&& (repoPro.findByUserId(professional) != null)) {
					
					LocalDateTime now = LocalDateTime.now();
					
					pay.setPrice(price.doubleValue());
					pay.setPayDay(now);
					pay.setClientId( repoCli.getLongClientId( client ) );
					pay.setProfessionalId( repoPro.getLongProfessionalId( professional ) );
					pay.setFormOfPayment(1L);
					
					repoPay.save(pay);
					
					return ResponseEntity.status(statusCode).body(responseBody);

				} else {

					if (repoCli.findByUserId(client) == null || repoCli.findByUserId(client).equals("null")) {
						return ResponseEntity.status(HttpStatus.NOT_FOUND)
								.body("Cliente não encontrado na base de dados...");

					} else {
						return ResponseEntity.status(HttpStatus.NOT_FOUND)
								.body("Profissional não encontrado na base de dados...");
					}
				}

			} else {
				return ResponseEntity.badRequest()
						.body("Os parâmetros de preço, cliente, e Profissional não podem ser nulos!.");
			}
		} else {
			return ResponseEntity.badRequest().body("Parâmetros de preço, cliente, e profissional são necessários.");
		}
	}

}
