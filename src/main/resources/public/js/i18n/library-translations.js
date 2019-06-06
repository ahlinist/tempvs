export const i18n = {
  en: {
    source: {
      properties: {
        name: 'Name',
        description: 'Description',
        classification: 'Classification',
        type: 'Type',
        period: 'Period'
      },
      types: {
        "": "-",
        WRITTEN: "Written",
        GRAPHIC: "Graphic",
        ARCHAEOLOGICAL: "Archaeological",
        OTHER: "Other"
      }
    },
    adminPage: {
      title: "Library - Admin",
      heading:  "Requested Authorities",
      user: "User",
      authority: "Authority",
      actions: "Actions"
    },
    welcomePage: {
      title: "Library",
      heading: "Historical periods",
    },
    periodPage: {
      sourceListHeading: "Sources",
      searchSourcePlaceholder: "Find source...",
      createSource: {
        popupButton: " New source",
        submitButton: "Create source",
        validation: {
          nameBlank: 'Please choose a name',
          classificationBlank: 'Please choose a classification',
          typeBlank: 'Please choose a type'
        }
      }
    },
    sourcePage: {
      title: 'Source',
      uploadImage: {
        imageLabel: 'Image',
        imageInfoLabel: 'Image Info',
        selectFileButton: 'Select file',
        submitButton: 'Submit'
      },
      deleteSource: {
        confirmation: 'Are you sure you want to delete this source?',
        yes: 'Yes',
        no: 'No'
      },
      deleteImage: {
        confirmation: 'Are you sure you want to delete this image?',
        yes: 'Yes',
        no: 'No'
      }
    },
    breadcrumb: {
      library: "Library",
      admin: "Admin"
    }
  }
}