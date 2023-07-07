package com.apitap.controller;

/**
 * Created by ashok-kumar on 26/7/16.
 */

public class ModelManager {

    SearchAddressManager searchAddressManager;
    AddProductSeen addProductSeen;
    AdsManager adsManager;
    MessageManager messageManager;
    HistoryManager historyManager;
    MerchantManager merchantManager;
    FavouriteManager favouriteManager;
    MerchantStoresManager merchantStoresManager;
    LoginManager loginManager;
    SignUpManager signUpManager;
    ForgotPasswordManager forgotManager;
    HomeManager homeManager;
    ItemManager itemManager;
    SpecialsManager specialsManager;
    DetailsManager detailsManager;
    ShoppingManager shoppingManager;
    CountryManager countryManager;
    AddressManager addressManager;
    ContactUsManger contactUsManger;
    ShoppingAssistantManager assistantManager;

    GetProductOptions getProductOptions;
    MerchantFavouriteManager merchantFavouriteManager;

    AddMerchantFavorite addMerchantFavorite;

    AddToFavoriteManager addToFavoriteManager;
    AddMerchantRating addMerchantRating;
    ShoppingCartManager shoppingCartManager;
    ShoppingCartItemManager shoppingCartItemManager;
    SearchManager searchManager;
    SearchItemsManager searchItemsManager;
    SearchFavoritesManager searchFavoritesManager;
    SearchNearByManager searchNearByManager;
    DeleteItemManager deleteItemManager;
    InvoiceManager invoiceManager;
    ReturnItemManager returnItemManager;
    CardManager cardManager;
    ReservationManager reservationManager;
    NotificationsManager notificationsManager;

    private static ModelManager modelManager;

    public ModelManager() {
        loginManager = new LoginManager();
        signUpManager = new SignUpManager();
        forgotManager = new ForgotPasswordManager();
        homeManager = new HomeManager();
        itemManager = new ItemManager();
        specialsManager = new SpecialsManager();
        contactUsManger = new ContactUsManger();
        searchAddressManager = new SearchAddressManager();

        reservationManager = new ReservationManager();
        addMerchantFavorite = new AddMerchantFavorite();
        cardManager = new CardManager();
        detailsManager = new DetailsManager();
        addressManager = new AddressManager();
        countryManager = new CountryManager();
        favouriteManager = new FavouriteManager();
        merchantManager = new MerchantManager();
        shoppingManager = new ShoppingManager();
        addToFavoriteManager = new AddToFavoriteManager();
        shoppingCartManager = new ShoppingCartManager();
        merchantStoresManager = new MerchantStoresManager();
        shoppingCartItemManager = new ShoppingCartItemManager();
        historyManager = new HistoryManager();
        messageManager = new MessageManager();
        adsManager = new AdsManager();
        addProductSeen = new AddProductSeen();
        searchManager = new SearchManager();
        searchItemsManager = new SearchItemsManager();
        searchFavoritesManager = new SearchFavoritesManager();
        searchNearByManager = new SearchNearByManager();
        deleteItemManager = new DeleteItemManager();
        invoiceManager = new InvoiceManager();
        returnItemManager = new ReturnItemManager();
        addMerchantRating = new AddMerchantRating();
        merchantFavouriteManager = new MerchantFavouriteManager();
        getProductOptions = new GetProductOptions();
        assistantManager = new ShoppingAssistantManager();
        notificationsManager = new NotificationsManager();
    }

    public static ModelManager getInstance() {
        if (modelManager == null)
            return modelManager = new ModelManager();
        else
            return modelManager;
    }

    public SpecialsManager getSpecialsManager() {
        return specialsManager;
    }

    public SearchAddressManager getSearchAddressManager() {
        return searchAddressManager;
    }


    public LoginManager getLoginManager() {
        return loginManager;
    }


    public GetProductOptions getProductOptions() {
        return getProductOptions;
    }


    public ShoppingAssistantManager getAssistantManager() {
        return assistantManager;
    }

    public NotificationsManager getNotificationsManager() {
        return notificationsManager;
    }

    public ReservationManager getReservationManager() {
        return reservationManager;
    }
    public CardManager getCardManager() {
        return cardManager;
    }

    public ContactUsManger getContactUsManger() {
        return contactUsManger;
    }

    public MerchantStoresManager getMerchantStoresManager() {
        return merchantStoresManager;
    }


    public AddMerchantFavorite getAddMerchantFavorite() {
        return addMerchantFavorite;
    }


    public AddProductSeen setProductSeen() {
        return addProductSeen;
    }


    public SignUpManager getSignUpManager() {
        return signUpManager;
    }

    public ForgotPasswordManager getforgotManager() {
        return forgotManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }


    public DetailsManager getDetailsManager() {
        return detailsManager;
    }

    public ShoppingManager getShoppingManager() {
        return shoppingManager;
    }

    public AddressManager getAddressManager() {
        return addressManager;
    }

    public MerchantFavouriteManager getMerchantFavouriteManager() {
        return merchantFavouriteManager;
    }

    public CountryManager getCountryManager() {
        return countryManager;
    }

    public FavouriteManager getFavouriteManager() {
        return favouriteManager;
    }

    public AddToFavoriteManager getAddToFavoriteManager() {
        return addToFavoriteManager;
    }

    public AddMerchantRating getAddMerchantRating() {
        return addMerchantRating;
    }

    public ShoppingCartManager getShoppingCartManager() {
        return shoppingCartManager;
    }

    public ShoppingCartItemManager getShoppingCartItemManager() {
        return shoppingCartItemManager;

    }

    public MerchantManager getMerchantManager() {
        return merchantManager;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public AdsManager getAdsManager() {
        return adsManager;
    }

    public SearchManager getSearchManager() {
        return searchManager;
    }


    public SearchItemsManager getSearchItemsManager() {
        return searchItemsManager;
    }

    public SearchFavoritesManager getSearchFavoritesManager() {
        return searchFavoritesManager;
    }

    public SearchNearByManager getSearchNearByManager() {
        return searchNearByManager;
    }

    public DeleteItemManager getDeleteItemManager() {
        return deleteItemManager;
    }

    public InvoiceManager getInvoiceManager() {
        return invoiceManager;
    }

    public ReturnItemManager getReturnItemManager() {
        return returnItemManager;
    }
}
