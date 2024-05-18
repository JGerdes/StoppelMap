import SwiftUI
import Shared

struct SettingsScreen: View {
    let viewModel = LicensesDependencies().with{
        LicensesViewModel(
            licensesRepository: $0.lincensesRepository
        )
    }
    let appInfo = LicensesDependencies().appInfo
    
    @State
    var licenseState: LicensesViewModel.ViewState = LicensesViewModel.ViewState()
    
    var body: some View {
        NavigationStack {
            List{
                Section(Res.strings().settings_disclaimer_title.desc().localized()) {
                    Text(Res.strings().settings_disclaimer_text.desc().localized())
                }
                Section(Res.strings().settings_info_title.desc().localized()) {
                    SettingsListItem(
                        headlineText: Res.strings().settings_info_version.desc().localized(),
                        trailingContent: {
                            VStack(alignment: .trailing){
                                Text(appInfo.versionName).font(.caption)
                                Text(appInfo.versionCode.description).font(.caption)
                            }
                        }
                    )
                    SettingsListItem(
                        headlineText: Res.strings().settings_info_build_type.desc().localized(),
                        trailingContent: {
                            Text(appInfo.buildType).font(.caption)
                        }
                    )
                    SettingsListItem(
                        headlineText: Res.strings().settings_info_commit.desc().localized(),
                        trailingContent: {
                            Text(appInfo.commitSha).font(.caption)
                        }
                    ).onTapGesture {
                        if let url = URL(
                            string: Res.strings().settings_repo_url_commit.format(args: [appInfo.commitSha]).localized()
                        ) {
                            UIApplication.shared.open(url)
                        }
                    }
                }
                Section(Res.strings().settings_libraries_title.desc().localized()) {
                    ForEach(licenseState.libraries, id: \.self) { library in
                        SettingsListItem(
                            headlineText: library.name,
                            supportingText: library.author,
                            trailingContent: {
                                Button {
                                    let url = URL(string: library.license.link)!
                                    UIApplication.shared.open(url)
                                } label: {
                                    Text(library.license.name)
                                }
                            },
                            onTap: {
                                if let sourceUrl = library.sourceUrl {
                                    if let url = URL(string: sourceUrl) {
                                        UIApplication.shared.open(url)
                                    }
                                }
                            }
                        )
                    }
                }
                Section(Res.strings().settings_images_title.desc().localized()) {
                    ForEach(licenseState.images, id: \.self) { image in
                        SettingsListItem(
                            headlineText: image.work,
                            supportingText: image.author,
                            trailingContent: {
                                if let license = image.license {
                                    Button {
                                        let url = URL(string: license.link)!
                                        UIApplication.shared.open(url)
                                    } label: {
                                        Text(license.name)
                                    }
                                }
                            },
                            onTap: {
                                if let sourceUrl = image.website {
                                    if let url = URL(string: sourceUrl) {
                                        UIApplication.shared.open(url)
                                    }
                                }
                            }
                        )
                    }
                }
                Section(content: {}, footer: {
                    Text(Res.strings().settings_statement.desc().localized())
                        .multilineTextAlignment(.center)
                        .frame(maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/, alignment: .center)
                })
            
            }
            .frame(
                maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/,
                maxHeight: .infinity,
                alignment: .top
            )
            .navigationBarTitle(Res.strings().settings_topbar_title.desc().localized(), displayMode: .large)
        }
        .task {
            for await state in viewModel.state {
                licenseState = state
            }
        }
    }
}

#Preview {
    SettingsScreen()
}

struct SettingsListItem<Content: View>: View {
    var headlineText: String
    var supportingText: String?
    var trailingContent: Content
    var onTap: (() -> Void)?
    
    init(headlineText: String, 
         supportingText: String? = nil,
         @ViewBuilder trailingContent: () -> Content,
         onTap: (() -> Void)? = nil
    ) {
        self.headlineText = headlineText
        self.supportingText = supportingText
        self.onTap = onTap
        self.trailingContent = trailingContent()
    }

    var body: some View {
        HStack(alignment: .center) {
            VStack(alignment: .leading) {
                Text(headlineText).font(.headline)
                if let supportingText = supportingText {
                    Text(supportingText).font(.caption)
                }
            }.onTapGesture {
                onTap?()
            }
            Spacer()
            trailingContent
            
        }
    }
}
