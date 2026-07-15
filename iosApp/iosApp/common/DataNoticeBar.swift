import SwiftUI
import Shared

struct DataNoticeBar: View {
    
    let viewModel = DataNoticeDependencies().with{
        DataNoticeViewModel(
            getDataNotice: $0.getDataNoticeUseCase,
        )
    }
    
    @State
    var viewState: DataNoticeViewModel.ViewState = DataNoticeViewModel.ViewState()
    
    var body: some View {
        VStack(spacing: 0) {
            ForEach(viewState.notices, id: \.self) { notice in
                HStack {
                    Image(systemName: "info.circle.fill")
                    Text(notice.title.forCurrentLanguage()).fontWeight(.bold)
                    Text(notice.content.forCurrentLanguage())
                }
                .padding()
                .cornerRadius(8)
            }
        }
        .task {
            for await state in viewModel.state {
                viewState = state
            }
        }
    }
}

