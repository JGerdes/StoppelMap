import SwiftUI
import Shared

struct NewsScreen: View {
    
    let viewModel = NewsDependencies().with{
        NewsViewModel(newsRepository: $0.newsRepository)
    }
    
    @State
    var viewState: NewsViewModel.ViewState = NewsViewModel.ViewState()
    
    var body: some View {
        NavigationStack {
            ScrollView(.vertical) {
                LazyVStack {
                    ForEach(viewState.articles, id: \.sortKeyString) { article in
                        ArticleCard(article: article)
                            .padding(.horizontal)
                            .padding(.bottom)
                    }
                    .task {
                        // Keep delay here, since we only want mark as shown after this view
                        // is shown for some while
                        try? await Task.sleep(nanoseconds: 2_000_000_000)
                        viewModel.onShowFirstArticle()
                    }
                    Color.clear
                        .onAppear{
                            viewModel.onListEndReached()
                        }
                }
            }
            .refreshable {
                do {
                    try await viewModel.forceRefresh()
                } catch {
                    
                }
            }
            .navigationBarTitle(Res.strings().news_topbar_title.desc().localized(), displayMode: .large)
        }
        .task {
            for await state in viewModel.state {
                viewState = state
            }
        }
    }
}

struct ArticleCard: View {
    var article: Article
    var body: some View {
        VStack(alignment: .leading) {
            if(article.images.count > 0) {
                TabView {
                    ForEach(article.images, id: \.uuid) { image in
                        ZStack(alignment: .bottomLeading) {
                            if let url = URL(string: image.url) {
                                Color.clear.overlay(
                                    CachedAsyncImage(url: url) {image in
                                        image
                                            .resizable()
                                            .scaledToFill()
                                    } placeholder: {
                                        if let placeholder = UIImage(blurHash: image.blurHash, size: CGSize(width: 40, height: 30)) {
                                            Image(uiImage: placeholder)
                                                .resizable()
                                                .scaledToFill()
                                        }
                                    }
                                ).clipped()
                            }
                            if let copyright = image.copyright {
                                Text(Res.strings().news_article_card_photo_copyright.format(args: [copyright]).localized())
                                    .font(.caption2)
                                    .frame(maxWidth: .infinity, alignment: .bottomLeading)
                                    .padding(.horizontal)
                                    .padding(.top, 64)
                                    .padding(.bottom, 4)
                                    .background(
                                        LinearGradient(
                                            gradient: Gradient(colors: [
                                                .black.opacity(0),
                                                .black.opacity(0.6)
                                            ]),
                                            startPoint: .top, endPoint: .bottom
                                        )
                                    )
                            }
                        }
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                    }
                }
                .aspectRatio(4.0/3.0, contentMode: /*@START_MENU_TOKEN@*/.fill/*@END_MENU_TOKEN@*/)
                .tabViewStyle(PageTabViewStyle())
                
            }
            VStack(alignment: .leading) {
                Text(article.publishDate.defaultFormat().localized()).font(.caption)
                Text(article.title).font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
                Text(article.teaser).font(.body)
                ZStack(alignment: .bottomTrailing) {
                    Button(Res.strings().news_article_card_more.desc().localized(), action: {
                        if let url = URL(string: article.url) {
                            UIApplication.shared.open(url)
                        }
                    }).buttonStyle(.bordered)
                }.frame(maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/, alignment: .trailing)
            }
            .padding()
            .frame(maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/, alignment: .leading)
        }
        .frame(maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/, alignment: .leading)
        .background(.thinMaterial)
        .cornerRadius(24.0)
    }
}

#Preview {
    NewsScreen()
}
