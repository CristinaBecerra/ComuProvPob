<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0">
    <xsl:output method="html"/>
    <xsl:param name="ciudad" select="X"></xsl:param>

    <xsl:template match="/">
        <html>
            <head>
                <link rel="stylesheet" href="miestilo.css"/>

            </head>
            <body>
                <h1><xsl:value-of select="$ciudad"/></h1>
                <xsl:for-each select="autonomicas/escrutinio_sitio">
                   
                    <xsl:if test="$ciudad='X' or $ciudad=nombre_sitio">      
                        <h2>Resultados de las Elecciones Autonómicas en: <xsl:value-of select="nombre_sitio"/>
                        </h2>
                        
                        <p/>
                        <table>
                            <xsl:attribute name="class">T1</xsl:attribute>
                            <caption>Datos Generales</caption>
                            <tr>
                                <th/>
                                <th>Votos</th>
                                <th>Abstenciones</th>
                                <th>Nulos</th>
                                <th>Blancos</th>
                            </tr>
                            <tr>
                                <td>Cantidad</td>
                                <td>
                                    <xsl:value-of select="votos/contabilizados/cantidad"/>
                                </td>
                                <td>
                                    <xsl:value-of select="votos/abstenciones/cantidad"/>
                                </td>
                                <td>
                                    <xsl:value-of select="votos/nulos/cantidad"/>
                                </td>
                                <td>
                                    <xsl:value-of select="votos/blancos/cantidad"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Porcentaje</td>
                                <td>
                                    <xsl:value-of select="votos/contabilizados/porcentaje"/>
                                </td>
                                <td>
                                    <xsl:value-of select="votos/abstenciones/porcentaje"/>
                                </td>
                                <td>
                                    <xsl:value-of select="votos/nulos/porcentaje"/>
                                </td>
                                <td>
                                    <xsl:value-of select="votos/blancos/porcentaje"/>
                                </td>
                            </tr>
                            
                        </table>
                        
                        <p/>
                        <table>
                            <xsl:attribute name="class">T1</xsl:attribute>
                            <caption>Resultados</caption>
                            <tr>
                                <th></th>
                                <th>Partido</th>
                                <th>Votos</th>
                                <th> % </th>
                                <th/>
                            </tr>
                            <xsl:for-each select="resultados/partido[votos_porciento>1]">
                                <xsl:variable name="ancho" select="string(round(votos_porciento*6))"/>
                                <tr>
                                    <xsl:choose>
                                        <xsl:when test="id_partido=22"><td class="{string('psoe')}"></td></xsl:when>
                                        <xsl:when test="id_partido=20"><td class="{string('pp')}"/></xsl:when>
                                        <xsl:when test="id_partido=6"><td class="{string('ciudadanos')}"/></xsl:when>
                                        <xsl:when test="id_partido=30"><td class="{string('vox')}"/></xsl:when>
                                        <xsl:when test="id_partido=1"><td class="{string('adelante')}"/></xsl:when>
                                        <xsl:when test="id_partido=15"><td class="{string('pacma')}"/></xsl:when>
                                        <xsl:otherwise><td></td></xsl:otherwise>
                                    </xsl:choose>
                                    <td>
                                        <xsl:value-of select="nombre"/>
                                    </td>
                                    <td>
                                        <xsl:value-of select="votos_numero"/>
                                    </td>
                                    <td>
                                        <xsl:value-of select="votos_porciento"/>
                                    </td>
                                    <td style="vertical-align:bottom;">
                                        <xsl:choose>
                                            <xsl:when test="id_partido=22"><div style="width:{$ancho}" class="{string('psoe')}"/></xsl:when>
                                            <xsl:when test="id_partido=20"><div style="width:{$ancho}" class="{string('pp')}"/></xsl:when>
                                            <xsl:when test="id_partido=6"><div style="width:{$ancho}" class="{string('ciudadanos')}"/></xsl:when>
                                            <xsl:when test="id_partido=30"><div style="width:{$ancho}" class="{string('vox')}"/></xsl:when>
                                            <xsl:when test="id_partido=1"><div style="width:{$ancho}" class="{string('adelante')}"/></xsl:when>
                                            <xsl:when test="id_partido=15"><div style="width:{$ancho}" class="{string('pacma')}"/></xsl:when>
                                            <xsl:otherwise><div style="width:{$ancho}"/></xsl:otherwise>
                                        </xsl:choose>
                                        
                                    </td>
                                </tr>
                                
                            </xsl:for-each>
                            
                            
                        </table>
                        
                    </xsl:if>
                </xsl:for-each>
            </body>
        </html>

    </xsl:template>

    <xsl:template match="escrutinio_sitio">
        
        
    </xsl:template>
</xsl:stylesheet>
