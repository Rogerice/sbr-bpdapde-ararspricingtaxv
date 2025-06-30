package com.santander.bp.app.mapper;

import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.external.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.Generated;
import org.springframework.stereotype.Component;

@Component
@Generated
public class PricingRequestMapper {

  /**
   * Constrói o corpo da requisição para o serviço de Pricing.
   *
   * @param apiRequest A requisição original que chegou na API, contendo dados do cliente.
   * @param cosmosOffers A lista de ofertas encontradas no CosmosDB, que serão precificadas.
   * @return Um objeto PricingRequest pronto para ser enviado ao serviço de pricing.
   */
  public PricingRequest buildFromCosmosOffers(
      OffersPricingRequest apiRequest, List<OffersPricingResponse> cosmosOffers) {
    if (cosmosOffers == null || cosmosOffers.isEmpty()) {
      return new PricingRequest(null, Collections.emptyList());
    }

    InvestmentOrder investmentOrder = buildInvestmentOrder(apiRequest);
    List<InvestmentPricingCondition> conditions = buildPricingConditions(cosmosOffers);

    return new PricingRequest(investmentOrder, conditions);
  }

  /** Constrói a seção 'investmentOrder' da requisição de pricing. */
  private InvestmentOrder buildInvestmentOrder(OffersPricingRequest apiRequest) {
    // ---- Construção de dentro para fora ----

    // CORREÇÃO: Mapeia "CPF" para o código "14", conforme o exemplo do Postman.
    String documentTypeCode =
        "CPF".equalsIgnoreCase(apiRequest.getDocumentType()) ? "14" : apiRequest.getDocumentType();
    Document document = new Document(apiRequest.getDocumentNumber(), documentTypeCode);

    Person person = new Person(document);
    Bank bank = new Bank("0033");
    Participant participant = new Participant(apiRequest.getCustomerId(), bank, person);
    Contract contract = new Contract(participant);
    Product productForContract = new Product(null, null, "151");
    InvestmentContract investmentContract = new InvestmentContract(contract, productForContract);
    Audit audit = new Audit("BP");

    return new InvestmentOrder(
        new InvestmentTradeChannel(apiRequest.getChannel()),
        new NetAmount(0.0, "BRL"),
        investmentContract,
        "R1",
        new PromotionalCode("000"),
        audit);
  }

  /** Constrói a lista de 'investmentPricingConditions' com base nas ofertas do CosmosDB. */
  private List<InvestmentPricingCondition> buildPricingConditions(
      List<OffersPricingResponse> cosmosOffers) {
    // Usado para gerar um ID sequencial (1, 2, 3...) para o 'price.code'.
    final AtomicInteger counter = new AtomicInteger(1);

    return cosmosOffers.stream()
        .map(
            offer -> {
              Subproduct subproduct = new Subproduct(offer.getSubProductCode());

              // Nome do produto é enviado vazio, conforme exemplo do Postman.
              // businessCategoryCode é removido, conforme correção anterior.
              Product productForCondition = new Product("", subproduct, null);

              // BenchmarkIndex é enviado vazio, conforme exemplo do Postman.
              BenchmarkIndex benchmarkIndex = new BenchmarkIndex("");

              // CORREÇÃO: O 'code' do preço agora é um sequencial.
              Price price =
                  new Price(
                      String.valueOf(counter.getAndIncrement()),
                      null,
                      Collections.singletonList(new Tier(new Term(0), null)));

              Term totalTerm = new Term(offer.getTerm() != null ? offer.getTerm() : 0);
              // Para 'totalTerm', usamos o 'prazo' se existir, ou 0 se for nulo.
              // O exemplo do Postman mostra que este valor pode ser variável.

              return new InvestmentPricingCondition(
                  productForCondition,
                  benchmarkIndex,
                  price,
                  new Term(0), // gracePeriod
                  totalTerm);
            })
        .collect(Collectors.toList());
  }
}
