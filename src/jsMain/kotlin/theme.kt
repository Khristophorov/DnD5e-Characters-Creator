package me.khrys.dnd.charcreator.client

import com.ccfraser.muirwik.components.styles.Theme
import com.ccfraser.muirwik.components.styles.createTheme

val defaultTheme = createDefaultTheme(js("""
    {
        components: {
            MuiButton: {
                styleOverrides: {
                    root: {
                        border: '3px solid red',
                        padding: '5px 15px',
                        borderRadius: '5px'
                    }
                }
            },
            MuiCircularProgress: {
                styleOverrides: {
                    root: {
                        position: 'fixed',
                        top: '0',
                        left: '0',
                        right: '0',
                        bottom: '0',
                        margin: 'auto',
                        zIndex: '3'
                    }
                }
            },
            MuiAvatar: {
                styleOverrides: {
                    root: {
                        width: '64px',
                        height: '64px'
                    }
                }
            },
            MuiPaper: {
                styleOverrides: {
                    root: {
                        backgroundColor: '#fff3e0'
                    }
                }
            },
            MuiDialog: {
                styleOverrides: {
                    paperWidthSm: {
                        maxWidth: '700px'
                    }
                }
            },
            MuiInputLabel: {
                styleOverrides: {
                    shrink: {
                        transformOrigin: 'top center',
                        overflow: 'visible'
                    }
                }
            },
            MuiOutlinedInput: {
                styleOverrides: {
                    root: {
                        minWidth: '100px'
                    }
                }
            },
            MuiTable: {
                styleOverrides: {
                    root: {
                        borderCollapse: 'separate'
                    }
                }
            },
            MuiTableCell: {
                styleOverrides: {
                    root: {
                        fontSize: 'medium',
                        border: '1px solid black !important',
                        textAlign: 'center'
                    }
                }
            }
        }
    }""")
)

fun createDefaultTheme(args: dynamic): Theme {
    val theme = createTheme(args = args)
    theme.palette.background.default = "#fff3e0"
    theme.palette.primary.main = "#ffcc80"
    theme.palette.primary.dark = "#ffb74d"
    theme.palette.primary.contrastText = "black"
    theme.palette.secondary.main = "#d97d1a"
    return theme
}
